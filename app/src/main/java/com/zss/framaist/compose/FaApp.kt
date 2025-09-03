package com.zss.framaist.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.zss.framaist.entrance.SmoothBottomNav
import com.zss.framaist.home.navigation.HomeRoute
import com.zss.framaist.home.navigation.MineRoute
import com.zss.framaist.navigation.FaNavHost

@Composable
fun FaApp(appState: FaAppState, modifier: Modifier = Modifier) {
    Scaffold(
        bottomBar = {
            val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            SmoothBottomNav(modifier, onClick = {
                when (it) {
                    0 -> appState.navController.navigate(HomeRoute) {
                        popUpTo(appState.navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }

                    else -> appState.navController.navigate(MineRoute) {
                        popUpTo(appState.navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            })
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                //.padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            FaNavHost(appState = appState)
        }
    }
}
