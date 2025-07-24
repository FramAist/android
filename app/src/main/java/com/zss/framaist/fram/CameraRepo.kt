package com.zss.framaist.fram

import android.graphics.Bitmap
import com.blankj.utilcode.util.TimeUtils
import com.zss.base.mvvm.BaseRepository
import com.zss.common.net.getOrNull
import com.zss.framaist.bean.AnalyzeResp
import com.zss.framaist.bean.SuggestionResp
import com.zss.framaist.net.GlobalApiManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class CameraRepo : BaseRepository() {
    val api = GlobalApiManager.composeApiService
    suspend fun analyzeRemote(data: Bitmap, ratio: String): AnalyzeResp? {
        val stream = ByteArrayOutputStream()
        data.compress(Bitmap.CompressFormat.JPEG, 30, stream)
        val byteArray = stream.toByteArray()
        val stream2 = ByteArrayOutputStream()
        val byteArray2 = stream2.toByteArray()
        data.compress(Bitmap.CompressFormat.JPEG, 10, stream)
        val rb = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                "image.jpg",
                byteArray.toRequestBody(("image/jpeg").toMediaTypeOrNull())
            )
            .addFormDataPart(
                "optimized_file",
                "image.jpg",
                byteArray2.toRequestBody(("image/jpeg").toMediaTypeOrNull())
            )
            .addFormDataPart("scene_type", "full")
            .addFormDataPart("created_at", TimeUtils.getNowString())
            .addFormDataPart("aspect_ratio", ratio)
            .build()
        return api.compositionAnalyze(rb).getOrNull()
    }

    suspend fun getSuggestion(taskId: String): SuggestionResp? {
        return api.getSuggestion(taskId).getOrNull()
    }
}