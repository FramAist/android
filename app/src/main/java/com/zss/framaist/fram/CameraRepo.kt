package com.zss.framaist.fram

import android.graphics.Bitmap
import com.zss.base.mvvm.BaseRepository
import com.zss.common.net.getOrNull
import com.zss.framaist.bean.AnalyzeResp
import com.zss.framaist.bean.SuggestionResp
import com.zss.framaist.net.GlobalApiManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class CameraRepo : BaseRepository() {
    val api = GlobalApiManager.composeApiService
    suspend fun analyzeRemote(data: Bitmap): AnalyzeResp? {
        val stream = ByteArrayOutputStream()
        data.compress(Bitmap.CompressFormat.JPEG, 30, stream)
        val byteArray = stream.toByteArray()
        val rb = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                "image.jpg",
                RequestBody.create(("image/jpeg").toMediaTypeOrNull(), byteArray)
            )
            .addFormDataPart("scene_type", "full")
            .addFormDataPart("created_at", "2025-07-14 10:00:00")
            .addFormDataPart("aspect_ratio", "16:9")
            .build()
        return api.compositionAnalyze(rb).getOrNull()
    }

    suspend fun getSuggestion(taskId: String): SuggestionResp? {
       return api.getSuggestion(taskId).getOrNull()
    }
}