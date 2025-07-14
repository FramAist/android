package com.zss.framaist.mine

import android.annotation.SuppressLint
import android.content.Intent
import com.zss.base.BaseActivity
import com.zss.base.util.setOnSingleClickedListener
import com.zss.common.util.MMKVUtil
import com.zss.framaist.databinding.ActivityUserInfoBinding

class UserInfoActivity : BaseActivity<ActivityUserInfoBinding>() {

    @SuppressLint("SetTextI18n")
    override fun initView() {
        val userInfo = MMKVUtil.getUserInfo() ?: return
        binding?.apply {
            tvNickName.text = userInfo.nickName
            tvNickName2.text = userInfo.nickName
            val phone = userInfo.phone ?: return
            tvPhone.text =
                phone.substring(0, 3) + "****" + phone.substring(7, phone.length)
        }
    }

    override fun initData() {
    }

    override fun bindingEvent() {
        super.bindingEvent()
        binding?.apply {
            ivModify.setOnSingleClickedListener {
                startActivity(Intent(this@UserInfoActivity, PswManagerActivity::class.java))
            }
            ivBack.setOnSingleClickedListener {
                finish()
            }
        }
    }
}