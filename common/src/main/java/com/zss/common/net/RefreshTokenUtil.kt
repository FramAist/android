package com.zss.common.net

import com.zss.base.util.LL
import com.zss.common.bean.RefreshTokenResp
import com.zss.common.util.MMKVUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object RefreshTokenUtil {
    //刷新token重新赋值token
    val baseApiService: BaseApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        MainBaseApiManager.getMainService("http://47.111.152.147:8888")
    }

    suspend fun refreshToken(scope: CoroutineScope) =
        suspendCancellableCoroutine<RefreshTokenResp?> { con ->
            scope.safeLaunch(onError = {
                LL.e("")
                if (it.message?.contains("HTTP 401") == true) {
                    //刷新TOKEN  还是401, 需要退出到登录页了
                } else {
                    con.resumeWithException(it)
                }
            }) {
                val jb = JSONObject().apply {
                    put("refresh_token", MMKVUtil.getRefreshToken())
                }
                val res =
                    baseApiService.refreshToken(MMKVUtil.getRefreshToken(), jb.toRequestBody())
                        .getOrNull()
                con.resume(res)
            }
        }
}