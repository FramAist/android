package com.zss.common.bean

data class LoginResp(
    var access_token: String?,
    var refresh_token: String?,
    val user: UserInfoBean?
)