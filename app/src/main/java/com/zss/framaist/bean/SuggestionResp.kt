package com.zss.framaist.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuggestionResp(
    val status: String,
    val suggestions: List<RecommendModel>?,
    var taskId: String?,
    val message: String?
) : Parcelable

enum class SuggestionStatus(val desc: String) {
    PENDING("pending"),
    FAILED("failed"),
    COMPLETED("completed"),
    PROCESSING("processing"),
    TIMEOUT("timeout")
}


