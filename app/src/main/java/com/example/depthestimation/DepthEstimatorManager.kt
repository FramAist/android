package com.example.depthestimation

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class DepthEstimatorManager(private val modelPath: String) {
    private var estimatorHandle: Long = 0
    private val depthEstimator = DepthEstimator()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        estimatorHandle = depthEstimator.nativeCreate(modelPath)
        estimatorHandle != 0L
    }

    suspend fun predictAsync(
        imagePath: String,
        depthThreshold: Float = 0.25f,
        areaThreshold: Float = 0.3f
    ): DepthAnalysisResult? = withContext(Dispatchers.IO) {
        if (estimatorHandle == 0L) return@withContext null

        depthEstimator.nativePredictFromFile(
            estimatorHandle, imagePath, null, depthThreshold, areaThreshold
        )
    }

    suspend fun predictFromBitmapAsync(
        bitmap: Bitmap,
        depthThreshold: Float = 0.25f,
        areaThreshold: Float = 0.3f
    ): DepthAnalysisResult? = withContext(Dispatchers.IO) {
        if (estimatorHandle == 0L) return@withContext null
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val imageData = stream.toByteArray()

        depthEstimator.nativePredictFromMemory(
            estimatorHandle, imageData, depthThreshold, areaThreshold
        )
    }

    fun destroy() {
        if (estimatorHandle != 0L) {
            depthEstimator.nativeDestroy(estimatorHandle)
            estimatorHandle = 0L
        }
        scope.cancel()
    }
}