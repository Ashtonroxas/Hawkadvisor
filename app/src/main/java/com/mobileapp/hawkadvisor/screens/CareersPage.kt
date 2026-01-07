package com.mobileapp.hawkadvisor.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mobileapp.hawkadvisor.navigation.Screen
import com.mobileapp.hawkadvisor.ui.components.GradientText
import com.mobileapp.hawkadvisor.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.mobileapp.hawkadvisor.R
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.mobileapp.hawkadvisor.utils.Career
import com.mobileapp.hawkadvisor.utils.loadCareersFromJson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareersPage(navController: NavController, majorName: String? = null) {
    val context = LocalContext.current
    val careers = remember {
        loadCareersFromJson(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.back_to_majors)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_majors)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))

                GradientText(
                    text = majorName ?: stringResource(R.string.major_placeholder),
                    style = TextStyle(
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.major_compare_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(careers) { career ->
                CareerCard(
                    career = career,
                    onClick = {
                        navController.navigate(Screen.Electives.createRoute(career.name))
                    }
                )
            }
        }
    }
}

@Composable
fun CareerCard(career: Career, onClick: () -> Unit) {
    val cardColor = MaterialTheme.colorScheme.secondary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardColor)
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = career.name,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.see_more),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Orange // Accent color
                    )
                )
            }

            Text(
                text = career.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "CareersPage Dark")
@Composable
fun CareersPagePreviewDark() {
    HawkAdvisorTheme {
        CareersPage(navController = rememberNavController())
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "CareersPage Light")
@Composable
fun CareersPagePreviewLight() {
    HawkAdvisorTheme {
        CareersPage(navController = rememberNavController())
    }
}
