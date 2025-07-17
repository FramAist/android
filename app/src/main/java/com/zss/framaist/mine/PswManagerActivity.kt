package com.zss.framaist.mine

import android.content.Intent
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.activity.viewModels
import com.hjq.shape.view.ShapeImageView
import com.zss.base.BaseActivity
import com.zss.base.util.setOnSingleClickedListener
import com.zss.base.util.toast
import com.zss.common.constant.IntentKey
import com.zss.common.net.safeLaunch
import com.zss.framaist.R
import com.zss.framaist.databinding.ActivityPswManagerBinding
import com.zss.framaist.login.LoginVM
import kotlinx.coroutines.Dispatchers

class PswManagerActivity : BaseActivity<ActivityPswManagerBinding>() {

    val vm: LoginVM by viewModels()

    override fun bindingEvent() {
        super.bindingEvent()
        binding?.apply {
            ivPswMark.setOnClickListener {
                ivPswMark.handlePswMarkClicked(etPsw)
            }
            ivNewPswMark.setOnClickListener {
                ivNewPswMark.handlePswMarkClicked(etNewPsw)
            }
            ivConfirmNewPswMark.setOnClickListener {
                ivConfirmNewPswMark.handlePswMarkClicked(etConfirmNewPsw)
            }
            tvSubmit.setOnSingleClickedListener {
                if (checkPsw(
                        etPsw.text.toString(),
                        etNewPsw.text.toString(),
                        etConfirmNewPsw.text.toString()
                    )
                ) {
                    safeLaunch(Dispatchers.Main, onError = {
                        gotoModifyResult(it.message)
                    }) {
                        val res = vm.modifyPsw(etPsw.text.toString(), etNewPsw.text.toString())
                        val errorInfo = if (!res) "请重新尝试,确保当前密码正确" else ""
                        gotoModifyResult(errorInfo)
                    }
                }
            }
            ivBack.setOnClickListener {
                finish()
            }
        }
    }

    fun checkPsw(cur: String, new: String, confirm: String): Boolean {
        try {
            require(cur.isNotEmpty()) { "当前密码不能为空!" }
            require(new.isNotEmpty()) { "新密码不能为空" }
            require(confirm.isNotEmpty()) { "再次确认密码不能为空" }
            require(cur != new) { "新密码与旧密码不能一样!" }
            require(new == confirm) { "两次新密码值不一样!" }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            toast(e.message ?: "操作失败!")
            return false
        }
    }

    private fun gotoModifyResult(des: String?) {
        startActivity(
            Intent(
                this@PswManagerActivity,
                PswModifyResultActivity::class.java
            ).apply {
                putExtra(IntentKey.ERROR_REASON, des)
            }
        )
    }

    fun ShapeImageView.handlePswMarkClicked(et: EditText) {
        val isHide = et.transformationMethod is PasswordTransformationMethod
        if (isHide) {
            // 显示密码
            et.inputType =
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or InputType.TYPE_CLASS_TEXT
            et.transformationMethod = null
            this.setImageResource(R.drawable.login_login_eye_open)
        } else {
            // 隐藏密码
            et.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            et.transformationMethod = PasswordTransformationMethod.getInstance()
            this.setImageResource(R.drawable.login_login_eye_close)
        }
        // 移动光标到末尾
        et.text?.length?.let { et.setSelection(it) }
    }
}