package com.zss.framaist.entrance

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.runtime.Composable
import com.zss.framaist.compose.BaseComposeActivity
import com.zss.framaist.fram.ui.navTo
import com.zss.framaist.login.LoginScreen
import com.zss.framaist.util.MMKVUtil
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class EntranceSplashActivity : BaseComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (MMKVUtil.isLogin()) {
            jumpToMain()
        }
    }

    @Composable
    override fun SetScreen() {
        LoginScreen(onLoginSuccess = {
            jumpToMain()
        })
    }

    fun jumpToMain() {
        navTo<EntranceMainActivity>()
        finish()
    }
}