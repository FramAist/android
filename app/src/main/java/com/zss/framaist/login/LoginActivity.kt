package com.zss.framaist.login

import android.content.Intent
import android.os.CountDownTimer
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.zss.base.BaseActivity
import com.zss.base.util.collectResumed
import com.zss.base.util.setOnSingleClickedListener
import com.zss.common.net.safeLaunch
import com.zss.common.util.MMKVUtil
import com.zss.framaist.R
import com.zss.framaist.common.showNotSupportedDialog
import com.zss.framaist.databinding.ActivityLoginBinding
import com.zss.framaist.entrance.EntranceMainActivity
import kotlin.math.roundToInt

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    val vm: LoginVM by viewModels()
    var mCountDownTimer: CountDownTimer? = null

    override fun bindingEvent() {
        super.bindingEvent()
        binding?.apply {
            tvForget.setOnClickListener {
                showNotSupportedDialog(this@LoginActivity)
            }
            tvPswLoginTab.setOnClickListener {
                vm.setLoginMode(MODE_PSW)
            }
            tvCodeLoginTap.setOnClickListener {
                vm.setLoginMode(MODE_PHONE)
            }
            ivPswMark.setOnClickListener {
                val isHide = etPsw.transformationMethod is PasswordTransformationMethod
                if (isHide) {
                    // 显示密码
                    etPsw.inputType =
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or InputType.TYPE_CLASS_TEXT
                    etPsw.transformationMethod = null
                    ivPswMark.setImageResource(R.drawable.login_login_eye_open)
                } else {
                    // 隐藏密码
                    etPsw.inputType =
                        InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    etPsw.transformationMethod = PasswordTransformationMethod.getInstance()
                    ivPswMark.setImageResource(R.drawable.login_login_eye_close)
                }
                // 移动光标到末尾
                etPsw.text?.length?.let { etPsw.setSelection(it) }
            }
            tvGetVerifyCode.setOnSingleClickedListener {
                showNotSupportedDialog(this@LoginActivity)
                return@setOnSingleClickedListener
                // 暂不支持
//                if (tvGetVerifyCode.text.toString() != "获取验证码") return@setOnSingleClickedListener
//                val phone = etAccount.text.toString()
//                safeLaunch {
//                    require(phone.isNotEmpty()) { "手机号码不能为空!" }
//                    val isInWhiteList = vm.checkWhiteList(phone)
//                    if (isInWhiteList) {
//                        val verifyRes = vm.getVerifyCode(phone)
//                        if (verifyRes) {
//                            toast("发送成功!")
//                            startCountDown()
//                        }
//                    }
//                    showWhiteListTips(!isInWhiteList)
//                }
            }
            tvLogin.setOnSingleClickedListener {
                val account = etAccount.text.toString()
                val psw = etPsw.text.toString()
                safeLaunch(onError = {
                    dismissLoading()
                }) {
                    require(account.isNotEmpty()) { "请输入${etAccount.hint}" }
                    require(psw.isNotEmpty()) { "请输入${etPsw.hint}!" }
                    showLoading()
                    val res = vm.login(account, psw)
                    MMKVUtil.setUserInfo(res)
                    startActivity(Intent(this@LoginActivity, EntranceMainActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun observe() {
        super.observe()
        vm.loginMode.collectResumed(this) { position ->
            binding?.apply {
                if (position == 0) {
                    showWhiteListTips(false)
                }
                val lastLoginAccount = MMKVUtil.getLastLoginAccount()
                if (!lastLoginAccount.isNullOrEmpty()) {
                    etAccount.setText(lastLoginAccount)
                }
                etAccount.hint = if (position == 0) "账号" else "手机号"
                etPsw.hint = if (position == 0) "密码" else "验证码"
                ivPswMark.isVisible = position == 0
                tvPswLoginTab.isSelected = position == 0
                tvCodeLoginTap.isSelected = position == 1
                indicator0.isSelected = position == 0
                indicator1.isSelected = position == 1
                tvGetVerifyCode.isGone = position == 0
                groupForgetPsw.isInvisible = position == 1
                val lp0 = indicator0.layoutParams as ViewGroup.LayoutParams
                lp0.height = if (position == 0) 8 else 2
                indicator0.layoutParams = lp0
                val lp1 = indicator1.layoutParams as ViewGroup.LayoutParams
                lp1.height = if (position == 1) 8 else 2
                indicator1.layoutParams = lp1
                indicator0.shapeDrawableBuilder.apply {
                    solidColor =
                        if (position == 0) getColor(com.zss.base.R.color.blue_375af6) else getColor(
                            com.zss.base.R.color.gray_394150
                        )
                    intoBackground()
                }
                indicator1.shapeDrawableBuilder.apply {
                    solidColor =
                        if (position == 1) getColor(com.zss.base.R.color.blue_375af6) else getColor(
                            com.zss.base.R.color.gray_394150
                        )
                    intoBackground()
                }
            }
        }
    }

    private fun showWhiteListTips(isShow: Boolean) {
        binding?.groupWhiteListTips?.isVisible = isShow
    }

    private fun startCountDown() {
        mCountDownTimer?.cancel()
        val tvTime = binding?.tvGetVerifyCode ?: return
        mCountDownTimer = object : CountDownTimer(10_000, 1000) {
            override fun onTick(p0: Long) {
                tvTime.text = (p0 / 1000.00).roundToInt().toString()
            }

            override fun onFinish() {
                tvTime.text = "获取验证码"
            }
        }
        mCountDownTimer?.start()
    }

}