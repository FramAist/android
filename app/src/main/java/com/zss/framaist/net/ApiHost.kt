package com.zss.framaist.net


object ApiHost {

    const val HOST_USER = "http://47.111.152.147:8888"
    const val HOST_COMPOSITION = "http://47.111.152.147:8889"

    const val HOST_RELEASE = "https://client-api.pxb7.com/api/"

    @JvmStatic
    fun getUserApiHost(): String {
        return HOST_USER
    }

    @JvmStatic
    fun getCompositionApiHost(): String {
        return HOST_COMPOSITION
    }

}