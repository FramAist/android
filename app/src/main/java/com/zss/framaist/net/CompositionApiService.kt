package com.zss.framaist.net

import com.zss.common.net.ApiResponse
import com.zss.framaist.bean.AnalyzeResp
import com.zss.framaist.bean.SuggestionResp
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 聊天模块api
 */
interface CompositionApiService {

    /**
     * 构图分析任务创建
     */
    @POST("v1/composition/analyze")
    suspend fun compositionAnalyze(@Body body: RequestBody): ApiResponse<AnalyzeResp?>

    /**
     * 获取构图建议
     */
    @GET("v1/composition/suggestions/{task_id}")
    suspend fun getSuggestion(@Path("task_id") taskId: String): ApiResponse<SuggestionResp?>

    /**
     * 确认构图建议
     */
    @POST("v1/composition/suggestions/confirm")
    suspend fun compositionConfirm(@Body body: RequestBody): ApiResponse<String?>

}