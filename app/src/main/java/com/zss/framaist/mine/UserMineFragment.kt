package com.zss.framaist.mine

import android.content.Intent
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.util.setOnDebouncedItemClick
import com.zss.base.ui.BaseFragment
import com.zss.base.util.LL
import com.zss.base.util.collectResumed
import com.zss.base.util.setOnSingleClickedListener
import com.zss.common.constant.IntentKey
import com.zss.common.util.MMKVUtil
import com.zss.framaist.databinding.UserFragmentMineBinding
import com.zss.framaist.fram.ui.navTo
import com.zss.framaist.home.RecommendDetailActivity
import com.zss.framaist.home.SOURCE_LIST
import com.zss.framaist.login.LoginActivity


class UserMineFragment : BaseFragment<UserFragmentMineBinding>() {

    private val mAdapter = RecentFramAdapter()

    private val vm: MineVM by viewModels()

    override fun fragmentInflater(): ((inflater: LayoutInflater) -> UserFragmentMineBinding) {
        return UserFragmentMineBinding::inflate
    }

    override fun initData() {
        vm.getRecentCompose()
    }

    override fun initView() {
        val userInfo = MMKVUtil.getUserInfo() ?: return
        _binding?.apply {
            rvHistory.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
            rvHistory.adapter = mAdapter
            val id = userInfo.user_id
            tvNickName.text = userInfo.nickName ?: "用户${id.substring(id.length - 6, id.length)}"
            tvDesc.text = "内测用户"
            mAdapter.setOnDebouncedItemClick { adapter, _, p ->
                requireActivity().navTo<RecommendDetailActivity> {
                    it.putExtra(IntentKey.DATA, adapter.getItem(p))
                    it.putExtra(IntentKey.SOURCE, SOURCE_LIST)
                }
            }
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
        _binding?.apply {
            vm.recentList.collectResumed(viewLifecycleOwner) {
                LL.e("xdd $it")
                mAdapter.submitList(it)
            }
        }
    }

}