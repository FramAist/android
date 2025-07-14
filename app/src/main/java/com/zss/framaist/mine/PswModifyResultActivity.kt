package com.zss.framaist.mine

import android.content.Intent
import androidx.core.graphics.toColorInt
import com.zss.base.BaseActivity
import com.zss.base.util.getResDrawable
import com.zss.common.constant.IntentKey
import com.zss.framaist.R
import com.zss.framaist.databinding.ActivityPswModifyResultBinding
import com.zss.framaist.entrance.EntranceMainActivity

class PswModifyResultActivity : BaseActivity<ActivityPswModifyResultBinding>() {

    override fun initView() {
        val errorReason = intent.getStringExtra(IntentKey.ERROR_REASON)
        val success = errorReason.isNullOrEmpty()
        binding?.apply {
            tvTitle.text = if (success) "密码修改成功" else "密码修改失败"
            tvContent.text = if (success) "您的密码已成功更新" else errorReason
            tvConfirm.text = if (success) "返回个人中心" else "重新尝试"
            ivStatus.setImageDrawable(
                if (success) getResDrawable(R.drawable.ic_success_green) else getResDrawable(
                    R.drawable.ic_failed_red
                )
            )
            ivStatus.shapeDrawableBuilder.apply {
                solidColor = if (success) {
                    "#283c2a".toColorInt()
                } else "#412524".toColorInt()
                intoBackground()
            }
        }
    }

    override fun initData() {
    }

    override fun bindingEvent() {
        super.bindingEvent()
        binding?.apply {
            tvConfirm.setOnClickListener {
                if (tvConfirm.text == "返回个人中心") {
                    startActivity(
                        Intent(this@PswModifyResultActivity, EntranceMainActivity::class.java)
                    )
                }
                finish()
            }
        }
    }

}