package com.zss.common.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfoBean(
    val user_id: String,
    val username: String?,
    val email: String?,
    val membership: String?,

    val nickName: String?,
    val phone: String?,
) : Parcelable