package com.ai.dietplanner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ai.dietplanner.data.UserData
import com.ai.dietplanner.screens.DietPlanScreen
import com.ai.dietplanner.screens.UserInputScreen

sealed class Screen(val route: String) {
    object UserInput : Screen("user_input")
    object DietPlan : Screen("diet_plan")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.UserInput.route
    ) {
        composable(Screen.UserInput.route) {
            UserInputScreen(
                onNavigateToDietPlan = { userData ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("userData", userData)
                    navController.navigate(Screen.DietPlan.route)
                }
            )
        }
        composable(Screen.DietPlan.route) {
            DietPlanScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 