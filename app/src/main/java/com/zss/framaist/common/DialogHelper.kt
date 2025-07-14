package com.zss.framaist.common

import android.content.Context
import com.lxj.xpopup.XPopup

fun showNotSupportedDialog(context: Context) {
    XPopup.Builder(context)
        .asConfirm("功能正在开发中,敬请期待!", "", "", "确认", {}, {}, true).show()
}