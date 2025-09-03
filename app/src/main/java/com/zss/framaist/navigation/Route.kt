package com.zss.framaist.navigation

sealed class Route(val route: String) {
    object ModifyPswResult : Route("modifyPswResult/{err}") {
        fun createRoute(err: String) = "modifyPswResult/$err"
    }
}