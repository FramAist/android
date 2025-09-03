package com.zss.framaist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zss.framaist.compose.FaAppState
import com.zss.framaist.compose.RecentFramScreen
import com.zss.framaist.home.navigation.HomeBaseRoute
import com.zss.framaist.home.navigation.MineRoute
import com.zss.framaist.home.navigation.homeSection
import com.zss.framaist.mine.MineScreen
import com.zss.framaist.mine.PswManagerScreen
import com.zss.framaist.mine.ResultScreen
import com.zss.framaist.mine.UserInfoScreen
import com.zss.framaist.mine.navigation.PasswordManagerRoute
import com.zss.framaist.mine.navigation.RecentListRoute
import com.zss.framaist.mine.navigation.UserInfoManagerRoute
import com.zss.framaist.mine.navigation.navigateToUserInfoManager

@Composable
fun FaNavHost(appState: FaAppState) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        builder = {
            homeSection()
            composable<MineRoute> {
                MineScreen(
                    onUserInfoClick = navController::navigateToUserInfoManager,
                    onRecentListClick = {
                        navController.navigate(RecentListRoute)
                    }
                )
            }
            composable<UserInfoManagerRoute> {
                UserInfoScreen(
                    navController::popBackStack,
                    onNavToPswManager = {
                        navController.navigate(PasswordManagerRoute)
                    })
            }
            composable<PasswordManagerRoute> {
                PswManagerScreen(navToResult = {
                    navController.navigate(Route.ModifyPswResult.createRoute(it))
                }, onPopBack = navController::popBackStack)
            }
            composable(
                route = Route.ModifyPswResult.route,
                arguments = listOf(
                    navArgument("err") {
                        type = NavType.StringType
                        nullable = true
                    }
                ),
            ) { backStackEntry ->
                val errReason = backStackEntry.arguments?.getString("err") ?: ""
                ResultScreen(errReason, {

                }, navController::popBackStack)
            }

            composable<RecentListRoute> {
                RecentFramScreen()
            }

        }
    )
}
