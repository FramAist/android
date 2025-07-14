package com.example.depthestimation

class DepthEstimator {

    // 创建深度估计器
    external fun nativeCreate(modelPath: String): Long

    // 销毁深度估计器
    external fun nativeDestroy(handle: Long)

    // 从文件路径预测
    external fun nativePredictFromFile(
        handle: Long,
        imagePath: String,
        outputPath: String?,
        depthThreshold: Float,
        areaThreshold: Float
    ): DepthAnalysisResult?

    // 从内存数据预测
    external fun nativePredictFromMemory(
        handle: Long,
        imageData: ByteArray,
        depthThreshold: Float,
        areaThreshold: Float
    ): DepthAnalysisResult?

    // 从Base64编码数据预测
    external fun nativePredictFromBase64(
        handle: Long,
        base64Data: String,
        depthThreshold: Float,
        areaThreshold: Float
    ): DepthAnalysisResult?

    companion object {

        init {
            try {
                System.loadLibrary("depth_estimation_jni")
                System.loadLibrary("depth_estimation_android")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 获取错误信息
        @JvmStatic
        external fun nativeGetLastError(): String?

        // 获取版本信息
        @JvmStatic
        external fun nativeGetVersion(): String?

        // 分析深度图
        @JvmStatic
        external fun nativeAnalyzeDepthMap(
            depthData: FloatArray,
            width: Int,
            height: Int,
            depthThreshold: Float,
            areaThreshold: Float
        ): DepthAnalysisResult?
    }
}