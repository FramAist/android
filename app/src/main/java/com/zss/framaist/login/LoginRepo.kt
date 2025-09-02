package com.zss.framaist.login

import com.zss.base.mvvm.BaseRepository
import com.zss.common.bean.LoginResp
import com.zss.common.net.getOrNull
import com.zss.common.net.toRequestBody
import com.zss.framaist.net.GlobalApiManager
import org.json.JSONObject
import javax.inject.Inject

class LoginRepo @Inject constructor() : BaseRepository() {
    private val api = GlobalApiManager.userApiService

    suspend fun loginRemote(phone: String, psw: String, isPsw: Boolean = true): LoginResp? {
        val jb = JSONObject().apply {
            if (isPsw) {
                put("email", phone)
                put("password", psw)
            } else {
                put("phone", phone)
                put("verify", psw)
            }
        }
        return api.login(jb.toRequestBody()).getOrNull()
    }

    suspend fun modifyPswRemote(oldPsw: String, newPsw: String): String? {
        val jb = JSONObject().apply {
            put("old_password", oldPsw)
            put("new_password", newPsw)
        }
        return api.modifyPsw(jb.toRequestBody()).getOrNull()
    }
}