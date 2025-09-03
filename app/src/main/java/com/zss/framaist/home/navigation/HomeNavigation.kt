package com.zss.framaist.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zss.framaist.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute // route t
@Serializable
data object MineRoute //

@Serializable
data object HomeBaseRoute // route to base navigation graph


fun NavController.navigateToHome(options: NavOptions) = navigate(route = HomeRoute, options)

fun NavGraphBuilder.homeSection() {
    navigation<HomeBaseRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen()
        }
    }
}