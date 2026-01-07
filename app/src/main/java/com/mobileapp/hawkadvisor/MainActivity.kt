package com.mobileapp.hawkadvisor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mobileapp.hawkadvisor.navigation.AppNavigation
import com.mobileapp.hawkadvisor.ui.theme.HawkAdvisorTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HawkAdvisorTheme {
                AppNavigation()
            }
        }
    }
}

