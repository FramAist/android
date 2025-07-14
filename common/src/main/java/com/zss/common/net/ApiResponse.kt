package com.zss.common.net

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author :chenjie
 * @createDate :2024/7/25 15:39
 * @description :
 * 接口调用返回数据结构体
 * 不限于网络请求
 */
open class ApiResponse<T> : Serializable {
    /*{ 兼容
    "success": true,
    "errCode": null,
    "errMessage": null,
    "data": null
}*/
    var code: String? = HttpStatusCode.REQUEST_SUCCESS
        get() = if (message == "success") {
            HttpStatusCode.REQUEST_SUCCESS
        } else {
            field ?: HttpStatusCode.REQUEST_FAILED
        }
    var message: String? = null
    var data: T? = null

    //---------以下分页才会返回-------------
    /** 每页数据条数*/
    var pageSize = 0

    /** 页码 */
    var pageIndex = 0

    /** 总页数 */
    var totalPages: Int = 0

    /** 总条数 */
    var totalCount = 0

    //----------------------
    constructor(code: String?, data: T) : this(code, null, data)
    constructor(code: String?, message: String?) : this(code, message, null)
    constructor(code: String?, message: String?, data: T?) {
        this.code = code
        this.message = message
        this.data = data
    }


    constructor(
        errCode: String?,
        errMessage: String?,
        data: T?,
        pageSize: Int,
        pageIndex: Int,
        totalCount: Int
    ) {
        this.code = errCode
        this.message = errMessage
        this.data = data
        this.pageSize = pageSize
        this.pageIndex = pageIndex
        this.totalCount = totalCount
    }

    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(HttpStatusCode.REQUEST_SUCCESS, null, data
            )
        }

        fun <T> next( code: String, message: String?, data: T?): ApiResponse<T> {
            return ApiResponse( code, message, data)
        }




    }


    data class PageDataList<T>(
        @SerializedName("page")
        val current: Int,
        @SerializedName("page_size")
        val pageSize: Int,
        val totalPages: Int,
        val total: Int,
        @SerializedName("list")
        val list: List<T>,
        val extra: JsonObject? = null
    ) : Serializable

    override fun toString(): String {
        return "ApiResponse( data=$data, pageSize=$pageSize, pageIndex=$pageIndex, totalPages=$totalPages, totalCount=$totalCount, code=$code)"
    }
}
