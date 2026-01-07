package com.mobileapp.hawkadvisor.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mobileapp.hawkadvisor.ui.theme.HawkAdvisorTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import com.mobileapp.hawkadvisor.ui.components.GradientText
import com.mobileapp.hawkadvisor.ui.theme.BlueDarkPrimary
import com.mobileapp.hawkadvisor.ui.theme.BlueLightPrimary
import com.mobileapp.hawkadvisor.ui.theme.Orange
import com.mobileapp.hawkadvisor.ui.theme.BlueDarkSecondary
import com.mobileapp.hawkadvisor.ui.theme.BlueLightSecondary
import com.mobileapp.hawkadvisor.ui.theme.BlueLightTertiary
import com.mobileapp.hawkadvisor.ui.theme.DifficultyEasy
import com.mobileapp.hawkadvisor.ui.theme.DifficultyHard
import com.mobileapp.hawkadvisor.ui.theme.DifficultyMedium
import androidx.compose.ui.res.stringResource
import com.mobileapp.hawkadvisor.R
import com.mobileapp.hawkadvisor.utils.Career
import com.mobileapp.hawkadvisor.utils.Elective
import com.mobileapp.hawkadvisor.utils.ProfessorInsight
import com.mobileapp.hawkadvisor.utils.loadCareersFromJson
import com.mobileapp.hawkadvisor.utils.loadElectivesFromJson
import com.mobileapp.hawkadvisor.utils.loadProfessorsFromJson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectivesPage(navController: NavController, career: String? = null) {

    val context = LocalContext.current
    val electives = remember { loadElectivesFromJson(context) }
    val professors = remember { loadProfessorsFromJson(context) }

    val allCareers = remember { loadCareersFromJson(context) }

    val currentCareer: Career = remember(career, allCareers) {
        val defaultCareerName = context.getString(R.string.career_placeholder)
        val targetName = career ?: defaultCareerName
        val foundCareer = allCareers.find { it.name == targetName }

        foundCareer ?: Career(
            name = targetName,
            description = context.getString(R.string.career_data_missing_desc, targetName),
            avgSalary = context.getString(R.string.career_data_missing_salary),
            growth = context.getString(R.string.career_data_missing_growth)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.back_to_careers)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_careers)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Career_Header_Electives(career = currentCareer)
            }

            item {
                Text(
                    text = stringResource(R.string.recommended_electives_title),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "${electives.size} courses",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(electives) { elective ->
                ElectiveCard(elective = elective)
            }

            item {
                Text(
                    text = stringResource(R.string.recommended_electives_title),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                ProfessorInsightsTable(professors = professors)
            }

            item {
                Text(
                    text = stringResource(R.string.other_careers_title),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun Career_Header_Electives(career: Career) {
    val gradientColors = if (isSystemInDarkTheme()) {
        listOf(
            BlueDarkSecondary,
            BlueLightPrimary,

        )
    } else {
        listOf(
            BlueLightSecondary,
            BlueDarkPrimary,
        )
    }

    val gradient = Brush.horizontalGradient(colors = gradientColors)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(gradient)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Career Name - Using GradientText
            GradientText(
                text = career.name,
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Career Description
            Text(
                text = career.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Salary and Growth Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Average Salary
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "💰",
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Text(
                            text = stringResource(R.string.career_header_salary_label),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = career.avgSalary,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Orange
                    )
                }

                // Growth Rate
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "📈",
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Text(
                            text = stringResource(R.string.career_header_growth_label),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = career.growth,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = BlueLightTertiary // Cyan accent
                    )
                }
            }
        }
    }
}

@Composable
fun ElectiveCard(elective: Elective) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Course Number and Difficulty
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = elective.courseNumber,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Orange // Using Orange from theme
                    )
                    DifficultyBadge(difficulty = elective.difficulty)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Course Name
                Text(
                    text = elective.courseName,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary // Changed to white for contrast on dark background
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Prerequisites
                if (elective.prerequisites.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "📚",
                            style = TextStyle(fontSize = 12.sp)
                        )
                        Text(
                            text = "Prerequisites: ${elective.prerequisites.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant // Light white for prerequisites
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Tags
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    elective.tags.take(3).forEach { tag ->
                        TagChip(tag = tag)
                    }
                }
            }

            // Credits and Add Button
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🎓 ${elective.credits} credits",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Light white for credits
                )

                IconButton(
                    onClick = { /* Handle add course */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_input_add),
                        contentDescription = "Add Course",
                        tint = Orange // Using Orange from theme
                    )
                }
            }
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: String) {
    val (backgroundColor, textColor) = when (difficulty) {
        "Easy" -> Pair(DifficultyEasy, Color.White)
        "Medium" -> Pair(DifficultyMedium, Color.White)
        "Hard" -> Pair(DifficultyHard, Color.White)
        else -> Pair(Color.Gray, Color.White)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        modifier = Modifier.padding(0.dp)
    ) {
        Text(
            text = difficulty,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            ),
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun TagChip(tag: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.padding(0.dp)
    ) {
        Text(
            text = tag,
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ProfessorInsightsTable(professors: List<ProfessorInsight>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Table Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Professor",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = "Course",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Rating",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Difficulty",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1.5f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Table Rows
            professors.forEach { professor ->
                ProfessorRow(professor = professor)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ProfessorRow(professor: ProfessorInsight) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Professor Name
        Text(
            text = professor.professorName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(2f)
        )

        // Course Number
        Text(
            text = professor.courseNumber,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFFFA726), // Orange color
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        // Rating with Star
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFFA726),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = professor.rating.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Difficulty Bar
        Column(
            modifier = Modifier.weight(1.5f)
        ) {
            DifficultyBar(difficulty = professor.difficulty)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = professor.difficulty.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DifficultyBar(difficulty: Double) {
    val progress = (difficulty / 5.0).toFloat()
    val color = when {
        difficulty <= 2.5 -> Color(0xFF4CAF50) // Green
        difficulty <= 3.5 -> Color(0xFFFFA726) // Orange
        else -> Color(0xFFF44336) // Red
    }

    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
        color = color,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Preview(showBackground = true, name = "Electives Page Preview", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ElectivesPagePreview() {
    HawkAdvisorTheme {
        ElectivesPage(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Electives Page Preview", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ElectivesPagePreview2() {
    HawkAdvisorTheme {
        ElectivesPage(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Career Header Preview", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CareerHeaderPreview() {
    HawkAdvisorTheme {
        Career_Header_Electives(
            career = Career(
                name = "Software Engineer",
                description = "Design, develop, and maintain software applications and systems. Work with code, databases, and APIs to create solutions for various platforms.",
                avgSalary = "$95K",
                growth = "+22%"
            )
        )
    }
}