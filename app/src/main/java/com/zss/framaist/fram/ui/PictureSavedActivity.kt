package com.zss.framaist.fram.ui

import android.content.Intent
import com.bumptech.glide.Glide
import com.zss.base.BaseActivity
import com.zss.base.util.setOnSingleClickedListener
import com.zss.framaist.databinding.ActivityPictureSavedBinding
import com.zss.framaist.entrance.EntranceMainActivity
import com.zss.framaist.fram.DataHelper

class PictureSavedActivity : BaseActivity<ActivityPictureSavedBinding>() {


    override fun initView() {

    }


    override fun initData() {
        binding?.apply {
            DataHelper.tempPicture.let {
                Glide.with(root.context).load(it).into(ivPicture)
            }
        }
    }

    override fun bindingEvent() {
        super.bindingEvent()
        binding?.apply {
            btnBack.setOnSingleClickedListener {
                DataHelper.tempPicture = null
                finish()
                startActivity(Intent(this@PictureSavedActivity, EntranceMainActivity::class.java))
            }
            btnContinue.setOnSingleClickedListener {
                DataHelper.tempPicture = null
                finish()
            }
        }
    }
}