package com.zss.framaist.entrance

import android.annotation.SuppressLint
import android.content.Intent
import com.zss.base.BaseActivity
import com.zss.common.util.MMKVUtil
import com.zss.framaist.databinding.ActivitySplashBinding
import com.zss.framaist.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class EntranceSplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun initView() {
        if (MMKVUtil.isLogin()) {
            jumpToMain()
        } else {
            jumpToLogin()
        }
    }

    override fun initData() {

    }

    fun jumpToMain() {
        startActivity(Intent(this, EntranceMainActivity::class.java))
        finish()
    }

    fun jumpToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}