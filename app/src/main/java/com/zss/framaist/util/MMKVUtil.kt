package com.zss.framaist.util

import com.tencent.mmkv.MMKV
import com.zss.common.bean.LoginResp
import com.zss.common.bean.UserInfoBean
import com.zss.common.constant.MMKVConstants
import com.zss.framaist.bean.UserData
import com.zss.framaist.mine.settings.DarkThemeConfig

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

    fun setUserData(data: UserData) {
        kv.encode(MMKVConstants.USER_DATA, data)
    }

    fun getUserData() =
        kv.decodeParcelable(MMKVConstants.USER_DATA, UserData::class.java) ?: UserData()

    fun setDarkThemeConfig(config: DarkThemeConfig) {
        setUserData(getUserData().copy(darkThemeConfig = config))
    }

    fun getDarkThemeConfig() = getUserData().darkThemeConfig

    fun getRefreshToken() = kv.decodeString(MMKVConstants.REFRESH_TOKEN) ?: ""

    fun getToken() = kv.decodeString(MMKVConstants.TOKEN) ?: ""

    fun getUserInfo(): UserInfoBean? {
        return kv.decodeParcelable<UserInfoBean>(MMKVConstants.USER_INFO, UserInfoBean::class.java)
    }

}