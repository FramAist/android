package com.zss.base.util

import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson


inline fun <reified T> Any.saveAs(): T {
    return this as T
}

@Suppress("UNCHECKED_CAST")
fun <T> Any.saveAsUnChecked(): T {
    return this as T
}

inline fun <reified T> Any.isEqualType(): Boolean {
    return this is T
}

fun Any?.toJson(): String {
    return Gson().toJson(this)
}

fun Any?.getInt(): Int {
    return (this.toString().toDoubleOrNull() ?: 0.0).toInt()
}

fun toast(msg: String) {
    ToastUtils.showShort(msg)
}

