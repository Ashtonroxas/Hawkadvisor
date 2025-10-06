"""
Project: HawkAdvisor
Task: 2.1 Develop the data ingestion script

Script: load_all.py
Goal: Process all scraped JSON files, de-duplicate data, and populate Azure SQL Database.

Tables:
  - Courses
  - Professors
  - RoadmapCourses
  - Prerequisites
"""

import os
import json
import glob
import re
import pyodbc

# 1.Establishes the connection to the Azure SQL database using environment variables
DB_SERVER = os.getenv("DB_SERVER", "hawk-advisor-sqlserver.database.windows.net")
DB_NAME   = os.getenv("DB_NAME", "hawkadvisor")
DB_USER   = os.getenv("DB_USER", "RiverHawk")  
DB_PASS   = os.getenv("DB_PASS", "AdviZooR26") 

conn = pyodbc.connect(
    "DRIVER={ODBC Driver 18 for SQL Server};"
    "SERVER=tcp:hawk-advisor-sqlserver.database.windows.net,1433;"
    "DATABASE=hawkadvisor;"
    "UID=RiverHawk;"
    "PWD=AdviZooR26;"
    "Encrypt=yes;TrustServerCertificate=no;Connection Timeout=30;"
)
cursor = conn.cursor()


# 2. Helper Functions
def clean_course_code(raw_code: str) -> str:
    """
    Normalize course codes:
    - Trim spaces
    - Handle combined codes like "ENGL.1010 / HONR.1100"
    - Always return the first valid course code
    """
    if not raw_code:
        return None
    match = re.findall(r"[A-Z]{2,5}\.\d{3,4}", raw_code)
    # Returns only the *first* match to handle combined codes.
    return match[0] if match else None


def upsert_course(course_code, title, credits):
    """
    Insert or fetch an existing Course.
    Returns CourseID.
    """
    try:
        cursor.execute(""" 
            INSERT INTO Courses (CourseCode, Title, Credits)
            VALUES (?, ?, ?);
            SELECT SCOPE_IDENTITY();
        """, (course_code, title, credits))
        course_id = cursor.fetchone()[0]
        conn.commit()
        return course_id
    except pyodbc.IntegrityError:
        # Catch the error, roll back, and then SELECT the existing course's ID.
        conn.rollback()
        cursor.execute("SELECT CourseID FROM Courses WHERE CourseCode = ?", (course_code,))
        row = cursor.fetchone()
        return row[0] if row else None


def upsert_professor(name, rating):
    """
    Insert or fetch an existing Professor.
    Returns ProfessorID.
    """
    try:
        cursor.execute("""
            INSERT INTO Professors (Name, Rating)
            VALUES (?, ?);
            SELECT SCOPE_IDENTITY();
        """, (name, rating))
        prof_id = cursor.fetchone()[0]
        conn.commit()
        return prof_id
    except pyodbc.IntegrityError:
        conn.rollback()
        cursor.execute("SELECT ProfessorID FROM Professors WHERE Name = ?", (name,))
        row = cursor.fetchone()
        return row[0] if row else None


def insert_roadmap_course(pathway_id, course_id, year, semester, sort_order):
    """
    Insert RoadmapCourse entry.
    """
    cursor.execute("""
        IF NOT EXISTS (
            SELECT 1 FROM RoadmapCourses 
            WHERE PathwayID = ? AND CourseID = ? AND YearNum = ? AND Semester = ? AND SortOrder = ?
        )
        INSERT INTO RoadmapCourses (PathwayID, CourseID, YearNum, Semester, SortOrder)
        VALUES (?, ?, ?, ?, ?)
    """, (pathway_id, course_id, year, semester, sort_order,
          pathway_id, course_id, year, semester, sort_order))
    conn.commit()


def insert_prerequisite(course_id, prereq_id):
    """
    Insert prerequisite relationship (idempotent).
    """
    cursor.execute("""
        IF NOT EXISTS (
            SELECT 1 FROM Prerequisites WHERE CourseID = ? AND PrereqID = ?
        )
        INSERT INTO Prerequisites (CourseID, PrereqID)
        VALUES (?, ?)
    """, (course_id, prereq_id, course_id, prereq_id))
    conn.commit()


def parse_prereq_string(prereq_text):
    """
    Extract prerequisite course codes from text.
    Example: "Prerequisite: MATH.1010 and COMP.1020"
    """
    return re.findall(r"[A-Z]{2,5}\.\d{3,4}", prereq_text or "")

# 3. Course List
master_courses = {}   # { "COMP.1010": {"title": ..., "credits": ..., "prereq_str": ...}, ... }

for filepath in glob.glob("data/*.json"):
    with open(filepath, "r", encoding="utf-8") as f:
        data = json.load(f)

    # Case A: pathway-style JSON (nested structure)
    if isinstance(data, dict) and "years" in data:
        for year_block in data["years"]:
            for sem_block in year_block["semesters"]:
                for course in sem_block["courses"]:
                    code = clean_course_code(course.get("course_number"))
                    if not code:
                        continue
                    master_courses[code] = {
                        "title": course.get("title", ""),
                        "credits": course.get("credits", 0),
                        "prereq_str": course.get("prerequisites", "")
                    }

    # Case B: electives or flat JSON
    elif isinstance(data, list):
        for course in data:
            code = clean_course_code(course.get("course_number"))
            if not code:
                continue
            if code not in master_courses:
                master_courses[code] = {
                    "title": course.get("course_name", ""),
                    "credits": course.get("credits", 0),
                    "prereq_str": course.get("prerequisites", "")
                }


# 4. Populate Core Tables
course_id_map = {}

for code, details in master_courses.items():
    cid = upsert_course(code, details["title"], details["credits"])
    if cid:
        course_id_map[code] = cid

# Professors
professor_id_map = {}
if os.path.exists("data/rmp_professors.json"):
    with open("data/rmp_professors.json", "r", encoding="utf-8") as f:
        professors = json.load(f)
        for prof in professors:
            pid = upsert_professor(prof["name"], prof.get("rating", None))
            if pid:
                professor_id_map[prof["name"]] = pid

# 5. Populate Relationships
# RoadmapCourses
for filepath in glob.glob("data/cs-*.json"):
    with open(filepath, "r", encoding="utf-8") as f:
        roadmap = json.load(f)

    pathway_name = roadmap.get("name", os.path.basename(filepath))
    cursor.execute("SELECT PathwayID FROM DegreePathways WHERE Name = ?", (pathway_name,))
    row = cursor.fetchone()
    if not row:
        continue
    pathway_id = row[0]

    year_num = 1
    for year_block in roadmap["years"]:
        for sem_block in year_block["semesters"]:
            semester = sem_block["name"]
            for sort_order, course in enumerate(sem_block["courses"], start=1):
                code = clean_course_code(course.get("course_number"))
                if not code or code not in course_id_map:
                    continue
                insert_roadmap_course(pathway_id, course_id_map[code], year_num, semester, sort_order)
        year_num += 1

# Prereqs
for code, details in master_courses.items():
    parent_id = course_id_map.get(code)
    if not parent_id:
        continue
    prereq_codes = parse_prereq_string(details.get("prereq_str", ""))
    for pre_code in prereq_codes:
        prereq_id = course_id_map.get(pre_code)
        if prereq_id:
            insert_prerequisite(parent_id, prereq_id)

print("✅ All data loaded successfully (Courses, Professors, Roadmaps, Prerequisites)")

# 6. Cleanup 
cursor.close()
conn.close()
