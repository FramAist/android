//package com.zss.common.net.interceptor
//
//
//
//import com.jeremyliao.liveeventbus.LiveEventBus
//import com.pxb7.net.api.HttpStatusCode
//import com.pxb7.base.baseUtils.MMKVUtil
//import com.pxb7.base.extension.toast
//import com.pxb7.base.util.AppGlobal
//import com.pxb7.common.constant.EventBusConstant
//import com.pxb7.common.event.MainTabEvent
//import com.therouter.TheRouter
//import kotlinx.coroutines.*
//import okhttp3.Interceptor
//import okhttp3.Response
//import okio.Buffer
//import okio.BufferedSource
//import org.json.JSONObject
//import java.io.IOException
//import java.nio.charset.Charset
//
//
//class TokenInterceptor : Interceptor {
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val originalRequest = chain.request()
//        val response = chain.proceed(originalRequest)
//        try {
//            val responseBody = response.body
//            val source: BufferedSource = responseBody!!.source()
//            source.request(Long.MAX_VALUE) // Buffer the entire body.
//            val buffer: Buffer = source.buffer()
//            val charset: Charset = Charset.forName("UTF-8")
//
//            val string: String = buffer.clone().readString(charset)
//            val jsonObjectResBack = JSONObject(string)
//            if (jsonObjectResBack.opt("errCode") == HttpStatusCode.TOKEN_FAILED_CODE){
//                //根据要求登录失效后跳转至首页
//                TheRouter.build("/entrance/main/notNeedLogin").navigation()
//                LiveEventBus.get<MainTabEvent>(EventBusConstant.EVENT_MAIN_TAB).post(MainTabEvent(0))
//                LiveEventBus.get<Boolean>(EventBusConstant.EVENT_LOGIN).post(false)
//                MMKVUtil.clear()
//                AppGlobal.sCurrentActivity.toast("登录失效，请重新登录")
//            }
////            else if (jsonObjectResBack.opt("errCode") == HttpStatusCode.REFRESH_FAILED_CODE ||jsonObjectResBack.opt("errCode")== HttpStatusCode.REFRESH_FAILED_CODE3 ||jsonObjectResBack.opt("errCode")== HttpStatusCode.TOKEN_FAILED_CODE1){
////                //                ExitLoginUtils.logout()
//////                AuthLoginUtils.sdkInit()
////                MMKVUtil.setToken("")
////                MMKVUtil.setLogin(false)
////                TheRouter.build("/login/login").navigation()//token失效跳转登录页
////            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return response
//        }
//        return response
//    }
//}