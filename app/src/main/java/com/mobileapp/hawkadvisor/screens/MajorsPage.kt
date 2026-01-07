package com.mobileapp.hawkadvisor.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mobileapp.hawkadvisor.ui.components.GradientText
import com.mobileapp.hawkadvisor.ui.theme.HawkAdvisorTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.foundation.clickable
import com.mobileapp.hawkadvisor.navigation.Screen
import androidx.compose.ui.res.stringResource
import com.mobileapp.hawkadvisor.R
import com.mobileapp.hawkadvisor.utils.Major
import com.mobileapp.hawkadvisor.utils.loadMajorsFromJson

@Composable
fun MajorsPage(navController: NavController) {
    val context = LocalContext.current
    val majorsList = remember {
        loadMajorsFromJson(context)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GradientText(
                text = stringResource(R.string.majors_page_title),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(8.dp),
            )

            // lazy column is used to make the view scrollable
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Space between cards
            ) {
                items(majorsList) { major ->
                    MajorCard(major = major, navController = navController)
                }
            }
        }
    }
}

@SuppressLint("LocalContextResourcesRead")
@Composable
fun MajorCard(major: Major, navController: NavController) {
    val context = LocalContext.current

    val baseResourceName = major.name
        .lowercase() // Convert to lowercase
        .replace(' ', '_')


    // icon file name will match the format: ic_major_name.png
    val iconId = remember(major.name) {
        val resourceName = "ic_$baseResourceName"
        context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val route = Screen.Careers.createRoute(major.name)
                navController.navigate(route)
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (iconId != 0) {
                    Image(
                        painter = painterResource(id = iconId),
                        contentDescription = "${major.name} Icon",
                        modifier = Modifier
                            .height(32.dp)
                            .width(32.dp)
                            .padding(end = 8.dp)
                    )
                }

                // Major Name Text
                Text(
                    text = major.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = major.careers,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, name = "Majors Screen Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MajorsScreenLight() {
    HawkAdvisorTheme {
        MajorsPage(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Majors Screen Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MajorsScreenDark() {
    HawkAdvisorTheme {
        MajorsPage(navController = rememberNavController())
    }
}