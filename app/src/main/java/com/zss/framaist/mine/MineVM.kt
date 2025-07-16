package com.zss.framaist.mine

import com.zss.base.mvvm.BaseVM
import com.zss.base.mvvm.launch
import com.zss.common.net.getOrNull
import com.zss.framaist.bean.RecommendModel
import com.zss.framaist.net.GlobalApiManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


/**
 * 接口少,直接略过repo层,不涉及UI的接口直接使用回调
 */
class MineVM : BaseVM<MineRepo>() {

    private val api = GlobalApiManager.composeApiService

    private val _recentList: MutableStateFlow<List<RecommendModel>> = MutableStateFlow(listOf())
    val recentList: StateFlow<List<RecommendModel>> = _recentList.asStateFlow()

    fun getRecentCompose() {
        launch({
            _recentList.value = api.getSuggestionList(20).getOrNull() ?: listOf()
        })
    }

}