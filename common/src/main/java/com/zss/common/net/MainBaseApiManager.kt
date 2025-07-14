package com.zss.common.net


import com.google.gson.Gson
import com.hjq.gson.factory.GsonFactory


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java


object MainBaseApiManager {
    //private var refreshTokenService: RefreshTokenService? = null

//    fun getRefreshTokenService(): RefreshTokenService {
//        if (refreshTokenService == null) {
//            synchronized(RefreshTokenService::class) {
//                if (refreshTokenService == null) {
//                    refreshTokenService = Retrofit.Builder()
//                        .client(GlobalOkHttpClient.defaultClientRefreshToken)
//                        .addConverterFactory(GsonConverterFactory.create(buildGson()))
//                        .baseUrl(ApiHost.getMainApiHost())
//                        .build()
//                        .create(RefreshTokenService::class.java)
//                }
//            }
//        }
//        return refreshTokenService!!
//    }

    inline fun <reified T> getMainService(host: String): T {
        return Retrofit.Builder()
            .client(GlobalOkHttpClient.mainClient)
            .addConverterFactory(GsonConverterFactory.create(buildGson()))
            .baseUrl(host)
            .build()
            .create(T::class.java)
    }

//    private inline fun <reified T> getDownloadService(): T {
//        return Retrofit.Builder()
//            .client(GlobalOkHttpClient.defaultDownloadClient)
//            .addConverterFactory(GsonConverterFactory.create(GsonFactory.getSingletonGson()))
//            .baseUrl(ApiHost.OSS_HOST)
//            .build()
//            .create(T::class.java)
//    }

    fun buildGson(): Gson {
        return GsonFactory.getSingletonGson()
    }
}

