package com.zss.framaist.fram

import android.graphics.Bitmap
import androidx.core.graphics.scale
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
        data.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteArray = stream.toByteArray()

        // 压缩至边长不超过960
        val scaledStream = ByteArrayOutputStream()
        val scaledBitmap = data.getScaledBitmap()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, scaledStream)
        val scaledByteArray = scaledStream.toByteArray()

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
                scaledByteArray.toRequestBody(("image/jpeg").toMediaTypeOrNull())
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

    /**
     * 压缩最大边长不超过960
     */
    fun Bitmap.getScaledBitmap(targetSize: Int = 720): Bitmap {
        val (targetWidth, targetHeight) = if (width > height) {
            targetSize to (height * targetSize / width)
        } else {
            (width * targetSize / height) to targetSize
        }
        return this.scale(targetWidth, targetHeight)
    }
}