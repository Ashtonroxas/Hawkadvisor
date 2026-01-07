/***********************************************
Authors: Vu, Ashton, Elijohn, Obadah
Date: Dec 8, 2025
Purpose: Final project for Mobile App II
What Learned: How to create an Android mobile app using Jetpack Compose and Kotlin
Sources of Help: Google websites to refer to Jetpack Compose syntax
Time Spent (Hours): 2 months
 ***********************************************/

/*
Mobile App Development I -- COMP.4630 Honor Statement
The practice of good ethical behavior is essential for maintaining good order
in the classroom, providing an enriching learning experience for students,
and training as a practicing computing professional upon graduation. This
practice is manifested in the University's Academic Integrity policy.
Students are expected to strictly avoid academic dishonesty and adhere to the
Academic Integrity policy as outlined in the course catalog. Violations will
be dealt with as outlined therein. All programming assignments in this class
are to be done by the student alone unless otherwise specified. No outside
help is permitted except the instructor and approved tutors.
I
certify that the work submitted with this assignment is mine and was
generated in a manner consistent with this document, the course academic
policy on the course website on Blackboard, and the UMass Lowell academic
code.
Date:  Dec 8, 2025
Names: Vu, Ashton, Elijohn, Obadah
*/

package com.mobileapp.hawkadvisor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mobileapp.hawkadvisor.screens.ElectivesPage
import com.mobileapp.hawkadvisor.screens.LandingPage
import com.mobileapp.hawkadvisor.screens.MajorsPage
import com.mobileapp.hawkadvisor.screens.CareersPage

sealed class Screen(val route: String) {
    data object Landing : Screen("landing_page")
    data object Majors : Screen("majors_page")
    data object Careers : Screen("career_page/{majorName}") {
        fun createRoute(majorName: String): String {
            return "career_page/$majorName"
        }
    }
    data object Electives : Screen("electives_page/{careerTitle}") {
        fun createRoute(careerTitle: String): String {
            return "electives_page/$careerTitle"
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Landing.route
    ) {
        // landing screen
        composable(Screen.Landing.route) {
            LandingPage(navController = navController)
        }

        // majors screen
        composable(Screen.Majors.route) {
            MajorsPage(navController = navController)
        }

        // careers screen
        composable(
            route = Screen.Careers.route,
            arguments = listOf(navArgument("majorName") {
                type = NavType.StringType
            })
        ) { backStackEntry -> val majorName = backStackEntry.arguments?.getString("majorName")
            CareersPage(
                navController = navController,
                majorName = majorName
            )
        }

        // electives screen
        composable(
            route = Screen.Electives.route, arguments = listOf(navArgument("careerTitle") {
                type = NavType.StringType
            })
        ) { backStackEntry -> val careerTitle = backStackEntry.arguments?.getString("careerTitle")
            ElectivesPage(
                navController = navController,
                career = careerTitle
            )
        }
    }
}