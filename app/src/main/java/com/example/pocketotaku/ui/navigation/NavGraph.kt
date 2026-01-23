package com.example.pocketotaku.ui.navigation

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pocketotaku.ui.detail.DetailScreen
import com.example.pocketotaku.ui.home.HomeScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = koinViewModel(),
                onAnimeClick = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                }
            )
        }
        
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
            DetailScreen(
                viewModel = koinViewModel(parameters = { parametersOf(animeId) }),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
