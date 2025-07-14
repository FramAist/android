package com.zss.common.net

/**
 * 请求状态码
 */
object HttpStatusCode {
    /**
     * 请求成功
     */
    const val REQUEST_SUCCESS = "200"

    /**
     * 请求失败
     */
    const val REQUEST_FAILED ="-1"

    /**
     * 用于Flow初始值，此状态不回调数据
     */
    const val REQUEST_EMPTY_FLOW = "-999"


    /**
     * token失效
     */
    const val TOKEN_FAILED_CODE = "00019"
    const val TOKEN_FAILED_CODE1  = 40001
    const val REFRESH_FAILED_CODE3 = 40003

    /**
     * refreshToken失效
     */
    const val REFRESH_FAILED_CODE = 20000

    /**
     * 账号禁用
     */
    const val ACCOUNT_BAN_CODE = "50003"
}