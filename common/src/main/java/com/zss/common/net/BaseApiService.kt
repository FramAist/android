package com.zss.common.net

import com.zss.common.bean.RefreshTokenResp
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

interface BaseApiService {
    @PUT("v1/auth/tokens")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): ApiResponse<RefreshTokenResp?>

}