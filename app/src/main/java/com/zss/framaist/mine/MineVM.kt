package com.zss.framaist.mine

import com.zss.base.mvvm.BaseVM
import com.zss.base.mvvm.launch
import com.zss.base.util.LL
import com.zss.common.net.getOrNull
import com.zss.framaist.bean.ConfirmedSuggestionResp
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

    private val _recentList: MutableStateFlow<List<ConfirmedSuggestionResp>> = MutableStateFlow(listOf())
    val recentList: StateFlow<List<ConfirmedSuggestionResp>> = _recentList.asStateFlow()

    fun getRecentCompose() {
        launch({
            val res = api.getSuggestionList(20).getOrNull()?.results ?: listOf()
            LL.e("xdd $res")
            _recentList.value = res
        },{
            LL.e("xdd $it")
        })
    }

}