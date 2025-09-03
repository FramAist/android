package com.zss.framaist.mine.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object UserInfoManagerRoute

@Serializable
data object PasswordManagerRoute

@Serializable
data object RecentListRoute

fun NavController.navigateToUserInfoManager(options: NavOptions? = null) =
    navigate(route = UserInfoManagerRoute, options)
