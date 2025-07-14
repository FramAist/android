package com.zss.framaist.net

import com.zss.common.net.ApiResponse
import com.zss.common.bean.LoginResp
import com.zss.common.bean.RefreshTokenResp
import com.zss.framaist.bean.AnalyzeResp
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApiService {

    @POST("ofs/mobile/contract/get/signatory")
    suspend fun getVerifyCode(@Body body: RequestBody): ApiResponse<Boolean?>

    @POST("ofs/mobile/contract/get/signatory")
    suspend fun checkWhiteList(@Body body: RequestBody): ApiResponse<Boolean?>

    @PUT("v1/auth/tokens")
    suspend fun refreshToken(@Body body: RequestBody): ApiResponse<RefreshTokenResp?>

    @POST("v1/auth/tokens")
    suspend fun login(@Body body: RequestBody): ApiResponse<LoginResp?>


    @PUT("v1/users/password")
    suspend fun modifyPsw(@Body body: RequestBody): ApiResponse<String?>

}