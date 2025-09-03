package com.zss.framaist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zss.framaist.compose.FaAppState
import com.zss.framaist.home.navigation.HomeBaseRoute
import com.zss.framaist.home.navigation.MineRoute
import com.zss.framaist.home.navigation.homeSection
import com.zss.framaist.mine.MineScreen

@Composable
fun FaNavHost(
    appState: FaAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        builder = {
            homeSection()
            composable<MineRoute> {
                MineScreen()
            }
            composable<MineRoute> {
                MineScreen()
            }
            composable<MineRoute> {
                MineScreen()
            }
            composable<MineRoute> {
                MineScreen()
            }
        }
    )
}
