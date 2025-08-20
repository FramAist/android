package com.zss.framaist.bean

enum class SuggestionStatus(val desc: String) {
    PENDING("pending"),
    FAILED("failed"),
    COMPLETED("completed"),
    PROCESSING("processing"),
    TIMEOUT("timeout")
}