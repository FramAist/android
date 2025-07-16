package com.zss.framaist.login

import com.zss.base.mvvm.BaseVM
import com.zss.base.mvvm.launch
import com.zss.common.bean.LoginResp
import com.zss.common.net.getOrNull
import com.zss.common.net.toRequestBody
import com.zss.framaist.net.GlobalApiManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val MODE_PSW = 0
const val MODE_PHONE = 1

/**
 * 接口少,直接略过repo层,不涉及UI的接口直接使用回调
 */
class LoginVM : BaseVM<LoginRepo>() {

    private val api = GlobalApiManager.userApiService

    private val _searchResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val searchResult = _searchResult.asStateFlow()


    private val _loginMode: MutableStateFlow<Int> = MutableStateFlow(MODE_PSW)
    val loginMode = _loginMode.asStateFlow()

    fun setLoginMode(mode: Int) {
        _loginMode.value = mode
    }


    suspend fun checkWhiteList(phone: String) = suspendCancellableCoroutine<Boolean> { con ->
        launch({
            val jb = JSONObject().apply {
                put("phone", phone)
            }
            delay(1000)
            con.resume(true)
//            val res = api.checkWhiteList(jb.toRequestBody()).getOrNull()
//            requireNotNull(res)
//            con.resume(res)
        }, {
            con.resumeWithException(Throwable(it))
        })
    }

    suspend fun getVerifyCode(phone: String) = suspendCancellableCoroutine<Boolean> { con ->
        launch({
            val jb = JSONObject().apply {
                put("phone", phone)
            }
            delay(100)
            con.resume(true)
            /*val res = api.getVerifyCode(jb.toRequestBody()).getOrNull()
            requireNotNull(res)
            con.resume(res)*/
        }, {
            con.resumeWithException(Throwable(it))
        })
    }

    /**
     * [isPsw]:true账号密码,false验证码.
     */
    suspend fun login(phone: String, psw: String, isPsw: Boolean = true) =
        suspendCoroutine<LoginResp> { con ->
            launch({
                val jb = JSONObject().apply {
                    if (isPsw) {
                        put("email", phone)
                        put("password", psw)
                    } else {
                        put("phone", phone)
                        put("verify", psw)
                    }
                }
                val res = api.login(jb.toRequestBody()).getOrNull()
                requireNotNull(res) { "登录失败!" }
                con.resume(res)
            }, { con.resumeWithException(Throwable(it)) })
        }

    suspend fun modifyPsw(old: String, new: String) = suspendCancellableCoroutine<Boolean> { con ->
        launch({
            val jb = JSONObject().apply {
                put("old_password", old)
                put("new_password", new)
            }
            api.modifyPsw(jb.toRequestBody()).getOrNull()
            con.resume(true)
        }, { con.resumeWithException(Throwable(it)) })
    }


    /**
     * 注册,暂未有入口
     */
    fun register(account: String, email: String, psw: String) {

    }

}