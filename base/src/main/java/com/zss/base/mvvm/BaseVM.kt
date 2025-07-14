package com.zss.base.mvvm

import com.zss.base.util.ClassUtil

open class BaseVM<T : BaseRepository> : BaseViewModel() {
    val repo: T by lazy {
        ClassUtil.getClass<T>(this).getDeclaredConstructor().newInstance()
    }
}