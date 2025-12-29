package com.lasalle.spaceapps.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lasalle.spaceapps.ui.screens.LoginScreen
import com.lasalle.spaceapps.ui.screens.RocketDetailScreen
import com.lasalle.spaceapps.ui.screens.RocketListScreen
import com.lasalle.spaceapps.ui.screens.SplashScreen

@Composable
fun SpaceAppsNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onNavigateToMain = {
                    navController.navigate("rocket_list") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("rocket_list") {
            RocketListScreen(
                onNavigateToDetail = { rocketId ->
                    navController.navigate("rocket_detail/$rocketId")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("rocket_list") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "rocket_detail/{rocketId}",
            arguments = listOf(navArgument("rocketId") { type = NavType.StringType })
        ) { backStackEntry ->
            val rocketId = backStackEntry.arguments?.getString("rocketId") ?: ""
            RocketDetailScreen(
                rocketId = rocketId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
