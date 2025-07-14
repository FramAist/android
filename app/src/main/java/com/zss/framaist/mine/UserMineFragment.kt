package com.zss.framaist.mine

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zss.base.ui.BaseFragment
import com.zss.base.util.setOnSingleClickedListener
import com.zss.common.util.MMKVUtil
import com.zss.framaist.databinding.UserFragmentMineBinding
import com.zss.framaist.login.LoginActivity


/**
 * @description :我的
 * TODO:
 * 1. 最近构图列表缺少数据源和跳转
 * 2. logout暴力清空了所有
 * 3. 除了用户信息其他item都没做跳转
 */
class UserMineFragment : BaseFragment<UserFragmentMineBinding>() {

    private val mAdapter = RecentFramAdapter()

    override fun fragmentInflater(): ((inflater: LayoutInflater) -> UserFragmentMineBinding) {
        return UserFragmentMineBinding::inflate
    }

    override fun initData() {

    }

    override fun initView() {
        val userInfo = MMKVUtil.getUserInfo() ?: return
        _binding?.apply {
            rvHistory.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
            rvHistory.adapter = mAdapter
            mAdapter.submitList(listOf("", "", ""))
            val id = userInfo.user_id
            tvNickName.text = userInfo.nickName ?: "用户${id.substring(id.length - 6, id.length)}"
            tvDesc.text = "内测用户"
        }
    }

    override fun bindingEvent() {
        binding.apply {
            clAccount.setOnSingleClickedListener {
                startActivity(Intent(requireActivity(), UserInfoActivity::class.java))
            }
            clLogout.setOnSingleClickedListener {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
                MMKVUtil.logout()
            }
        }
    }

    override fun observe() {

    }

}