package com.example.depthestimation

import android.graphics.Bitmap

data class DepthAnalysisResult(
    val isTooClose: Boolean,        // 是否过于接近
    val percentageTooClose: Float,  // 过近像素占比
    val minDepth: Float,            // 最小深度值
    val maxDepth: Float,            // 最大深度值
    val meanDepth: Float,           // 平均深度值
    val threshold: Float            // 判断阈值
) {
    var bitmap: Bitmap? = null
    fun isNormal() = !isTooClose

    fun getDistanceStatus(): String = if (isTooClose) "过近" else "正常"

    fun getPercentageString(): String = "${(percentageTooClose * 100).toInt()}%"
}
