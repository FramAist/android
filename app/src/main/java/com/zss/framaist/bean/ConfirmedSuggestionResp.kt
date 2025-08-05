package com.zss.framaist.bean

data class ConfirmedSuggestionResp(
    val task_id: String?,
    val suggestion_id: String?,
    val image_url: String?,
    val scene_type: String?,
    val score: Float?,
    val rank: Int?,
    val model_version: String?,
    val saved_at: String?
)

enum class SceneType() {
    LONG, FULL, MEDIUM
}