package com.zss.common.util

import com.tencent.mmkv.MMKV
import com.zss.common.bean.LoginResp
import com.zss.common.bean.UserInfoBean
import com.zss.common.constant.MMKVConstants

object MMKVUtil {
    val kv = MMKV.defaultMMKV()

    fun isLogin() = !kv.decodeString(MMKVConstants.TOKEN).isNullOrEmpty()

    fun logout() {
        val lastLoginAccount = getLastLoginAccount()
        kv.clear()
        kv.encode(MMKVConstants.LAST_LOGIN_ACCOUNT, lastLoginAccount)
    }

    fun getLastLoginAccount() = kv.decodeString(MMKVConstants.LAST_LOGIN_ACCOUNT)


    fun setUserInfo(res: LoginResp) {
        kv.encode(MMKVConstants.USER_INFO, res.user)
        kv.encode(MMKVConstants.TOKEN, res.access_token)
        kv.encode(MMKVConstants.REFRESH_TOKEN, res.refresh_token)
        kv.encode(MMKVConstants.LAST_LOGIN_ACCOUNT, res.user?.email)
    }

    fun getRefreshToken() = kv.decodeString(MMKVConstants.REFRESH_TOKEN) ?: ""

    fun getToken() = kv.decodeString(MMKVConstants.TOKEN) ?: ""

    fun getUserInfo(): UserInfoBean? {
        return kv.decodeParcelable<UserInfoBean>(MMKVConstants.USER_INFO, UserInfoBean::class.java)
    }

}