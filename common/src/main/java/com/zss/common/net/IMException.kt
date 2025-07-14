package com.zss.common.net

data class IMException(
    val errCode: String,
    val errMsg: String? = ""
) : Exception(errMsg)