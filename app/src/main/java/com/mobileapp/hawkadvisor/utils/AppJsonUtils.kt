package com.mobileapp.hawkadvisor.utils

import android.content.Context
import com.mobileapp.hawkadvisor.R
import kotlinx.serialization.json.Json
import java.io.InputStreamReader
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

@Serializable
data class Major(
    val name: String,
    val careers: String
)

@Serializable
data class Career(
    val name: String,
    val description: String,
    val avgSalary: String,
    val growth: String
)

@Serializable
data class Elective(
    val courseNumber: String,
    val courseName: String,
    val difficulty: String,
    val credits: Int,
    val prerequisites: List<String>,
    val tags: List<String>
)

@Serializable
data class ProfessorInsight(
    val professorName: String,
    val courseNumber: String,
    val rating: Double,
    val difficulty: Double // 1.0 to 5.0
)

fun loadMajorsFromJson(context: Context): List<Major> {
    val inputStream = context.resources.openRawResource(com.mobileapp.hawkadvisor.R.raw.majors_data)
    val reader = InputStreamReader(inputStream)
    val jsonString = reader.readText()
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(ListSerializer(Major.serializer()), jsonString)
}

fun loadElectivesFromJson(context: Context): List<Elective> {
    val inputStream = context.resources.openRawResource(R.raw.electives_data)
    val reader = InputStreamReader(inputStream)
    val jsonString = reader.readText()
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(ListSerializer(Elective.serializer()), jsonString)
}

fun loadProfessorsFromJson(context: Context): List<ProfessorInsight> {
    val inputStream = context.resources.openRawResource(R.raw.professors_data)
    val reader = InputStreamReader(inputStream)
    val jsonString = reader.readText()
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(ListSerializer(ProfessorInsight.serializer()), jsonString)
}

fun loadCareersFromJson(context: Context): List<Career> {
    val inputStream = context.resources.openRawResource(R.raw.careers_data)
    val reader = InputStreamReader(inputStream)
    val jsonString = reader.readText()
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(ListSerializer(Career.serializer()), jsonString)
}