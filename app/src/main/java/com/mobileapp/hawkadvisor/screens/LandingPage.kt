// LandingPage.kt 
package com.mobileapp.hawkadvisor.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mobileapp.hawkadvisor.ui.theme.HawkAdvisorTheme
import androidx.navigation.compose.rememberNavController
import com.mobileapp.hawkadvisor.R
import com.mobileapp.hawkadvisor.navigation.Screen
import com.mobileapp.hawkadvisor.ui.components.GradientButton
import com.mobileapp.hawkadvisor.ui.components.GradientText

@Composable
fun LandingPage(navController: NavHostController) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Welcome Text
            GradientText(
                text = stringResource(R.string.landing_page_title),
                style = TextStyle(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Mascot
            Image(
                painter = painterResource(id = R.drawable.hawk_advisor_mascot),
                contentDescription = "",
                modifier = Modifier.width(350.dp).height(350.dp),
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Description Text
            Text(
                text = stringResource(R.string.landing_page_description),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))

            GradientButton(
                onClick = {
                    navController.navigate(Screen.Majors.route)
                }
            ) {
                Text(
                    stringResource(R.string.landing_page_button_get_started),
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    style = TextStyle(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Landing Screen Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MyLandingPageLight() {
    HawkAdvisorTheme {
        LandingPage(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Landing Screen Light", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyLandingPageDark() {
    HawkAdvisorTheme {
        LandingPage(navController = rememberNavController())
    }
}