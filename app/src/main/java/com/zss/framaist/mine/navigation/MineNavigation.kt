package com.zss.framaist.mine.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import okhttp3.Route

@Serializable
data object UserInfoManagerRoute

@Serializable
data object PasswordManagerRoute


fun NavController.navigateToUserInfoManager(options: NavOptions? = null) =
    navigate(route = UserInfoManagerRoute, options)
