package com.zss.framaist.net

import com.zss.common.net.MainBaseApiManager

object GlobalApiManager {

    val userApiService: UserApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        MainBaseApiManager.getMainService(ApiHost.getUserApiHost())
    }

    val composeApiService: CompositionApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        MainBaseApiManager.getMainService(ApiHost.getCompositionApiHost())
    }
}