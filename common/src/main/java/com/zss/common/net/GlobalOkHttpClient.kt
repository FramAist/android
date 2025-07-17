package com.zss.common.net


import com.zss.base.util.LL
import com.zss.common.net.interceptor.HttpInfoCatchInterceptor
import com.zss.common.util.MMKVUtil
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object GlobalOkHttpClient {
    /**
     * 请求头处理拦截器
     * 添加公共参数到请求头中
     *
     *
     */
    private val headerInterceptor: Interceptor by lazy {
        Interceptor { chain ->
            try {
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${MMKVUtil.getToken()}")
                    .addHeader("Content-Type", "application/json;charset=utf-8")
                    .build()
                chain.proceed(request)
            } catch (e: IOException) {
                LL.e("xdd err $e  ")
                e.printStackTrace()
                Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(400)
                    .message(e.message ?: "Network error")
                    .body(
                        ResponseBody.create(
                            "text/plain".toMediaType(),
                            "Network error: ${e.message}"
                        )
                    )
                    .build()
            } catch (e: Exception) {
                LL.e("xdd err $e  ")
                e.printStackTrace()
                Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(400)
                    .message(e.message ?: "Network error")
                    .body(
                        ResponseBody.create(
                            "text/plain".toMediaType(),
                            "Network error: ${e.message}"
                        )
                    )
                    .build()
            }
        }
    }

    /**
     * http打印拦截器
     */
    private val httpInfoCatchInterceptor: HttpInfoCatchInterceptor by lazy {
        val interceptor = HttpInfoCatchInterceptor()
        interceptor.setCatchEnabled(true)
        interceptor.setHttpInfoCatchListener {
            it.logOut(true)
        }
        return@lazy interceptor
    }

    /**
     * http打印拦截器 不打印body
     */
    private val ossHttpInfoCatchInterceptor: HttpInfoCatchInterceptor by lazy {
        val interceptor = HttpInfoCatchInterceptor()
        interceptor.setCatchEnabled(true)
        interceptor.setHttpInfoCatchListener {
            it.logOut(false)
        }
        return@lazy interceptor
    }


    /**
     * 默认OkHttpClient(通用请求)
     */
    val defaultClient: OkHttpClient by lazy {
        val builder = getBuilder()
        return@lazy builder.sslSocketFactory(
            sslContext.socketFactory,
            trustAllCerts[0] as X509TrustManager
        )
            .hostnameVerifier { _, _ -> true } // 忽略主机名验证
            .build()
    }

    /**
     * 主请求(业务请求)
     */
    val mainClient: OkHttpClient by lazy {
        val mainBuilder = getMainBuilder()
        return@lazy mainBuilder.sslSocketFactory(
            sslContext.socketFactory,
            trustAllCerts[0] as X509TrustManager
        )
            .hostnameVerifier { _, _ -> true } // 忽略主机名验证
            .build()
    }

//    /**
//     * 刷新token
//     */
//    val defaultClientRefreshToken: OkHttpClient by lazy {
//        return@lazy getRefreshTokenBuilder().sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
//            .hostnameVerifier { _, _ -> true } // 忽略主机名验证
//            .build()
//    }

    /**
     * 下载
     */
//    val defaultDownloadClient: OkHttpClient by lazy {
//        return@lazy getDownloadBuilder().sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
//            .hostnameVerifier { _, _ -> true } // 忽略主机名验证
//            .build()
//    }

    /**
     * Oss
     */
//    val ossUploadClient by lazy {
//        val mainBuilder = getOssBuilder()
//        return@lazy mainBuilder.sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
//            .hostnameVerifier { _, _ -> true } // 忽略主机名验证
//            .build()
//    }

//    private fun getOssBuilder(): OkHttpClient.Builder {
//        return OkHttpClient.Builder()
//            .readTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .addInterceptor(headerInterceptor)
//            .addInterceptor(TokenInterceptor())
//            .addNetworkInterceptor(ossHttpInfoCatchInterceptor) // 设置日志拦截器，打印接口请求日志
//    }

    fun getBuilder(): OkHttpClient.Builder {
        val builder = try {
            OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                //            .cache(
                //                Cache(
                //                    com.pxb7.base.util.AppGlobal.sApplication.cacheDir,
                //                    1024 * 1024 * 10
                //                )
                //            ) // 10M大小的缓存
                .addInterceptor(headerInterceptor) // 设置添加公共请求头的拦截器
            //.addInterceptor(TokenInterceptor())
            //.addNetworkInterceptor(httpInfoCatchInterceptor)
        } catch (e: Exception) {
            e.printStackTrace()
            OkHttpClient.Builder()
        }
        return builder
    }

    private fun getMainBuilder(): OkHttpClient.Builder {
        val mainBuilder = try {
            OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                // .addInterceptor(CommonQueryParamInterceptor())
                .addInterceptor(headerInterceptor)
                //.addInterceptor(TokenInterceptor())
                .addNetworkInterceptor(httpInfoCatchInterceptor)
        } catch (e: Exception) {
            LL.e("xdd $e")
            e.printStackTrace()
            OkHttpClient.Builder()
        }
        return mainBuilder
    }

//    private fun getDownloadBuilder(): OkHttpClient.Builder {
//        return OkHttpClient.Builder()
//            .readTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .cache(Cache(AppGlobal.sApplication.cacheDir, 1024 * 1024 * 10)) // 10M大小的缓存
//            .addInterceptor(headerInterceptor) // 设置添加公共请求头的拦截器
//            .addNetworkInterceptor(httpInfoCatchInterceptor) // 设置日志拦截器，打印接口请求日志
//    }

//
//    private fun getRefreshTokenBuilder(): OkHttpClient.Builder {
//        return OkHttpClient.Builder()
//            .readTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .addInterceptor(RefreshTokenInterceptor())
//            .addNetworkInterceptor(httpInfoCatchInterceptor) // 设置日志拦截器，打印接口请求日志
//    }

    // 创建一个不安全的 TrustManager
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

        override fun checkClientTrusted(
            chain: Array<out X509Certificate?>?,
            authType: String?
        ) {
        }

        override fun checkServerTrusted(
            chain: Array<out X509Certificate?>?,
            authType: String?
        ) {
        }

        override fun getAcceptedIssuers(): Array<out X509Certificate?>? =
            arrayOf()
    })

    // 初始化 SSLContext
    val sslContext = SSLContext.getInstance("TLS").apply {
        init(null, trustAllCerts, SecureRandom())
    }


}