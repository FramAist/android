package com.zss.framaist.entrance

import android.annotation.SuppressLint
import com.zss.base.BaseActivity
import com.zss.framaist.util.MMKVUtil
import com.zss.framaist.databinding.ActivitySplashBinding
import com.zss.framaist.fram.ui.navTo
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
        navTo<EntranceMainActivity>()
        finish()
    }

    fun jumpToLogin() {
        navTo<LoginActivity>()
        finish()
    }
}