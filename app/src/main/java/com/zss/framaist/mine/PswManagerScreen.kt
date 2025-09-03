package com.zss.framaist.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zss.base.util.LL
import com.zss.base.util.toast
import com.zss.framaist.R
import com.zss.framaist.compose.BackTitleCard
import com.zss.framaist.compose.ConfirmButton
import com.zss.framaist.compose.ui.theme.FramAistTheme
import com.zss.framaist.login.LoginVM

@Composable
fun ModifyPswCard(
//    curPsw: String,
//    newPsw: String,
//    confirmPsw: String,
    onCurChange: (String) -> Unit,
    onNewChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    navToResult: (String) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = colorResource(com.zss.base.R.color.black_33))
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        InputCard("当前密码", hint = "请输入当前密码", onValueChange = {
            onCurChange(it)
        }, modifier)
        Spacer(modifier.height(10.dp))
        InputCard("新密码", hint = "请输入新密码", onValueChange = {
            onNewChange(it)
        })
        Spacer(modifier.height(8.dp))
        Text(
            text = "不少于8位,至少包含1个数字和1个字母",
            color = colorResource(com.zss.base.R.color.gray_9da3ae),
            fontSize = 12.sp
        )
        Spacer(modifier.height(16.dp))
        InputCard("确认新密码", hint = "请再次输入新密码", onValueChange = {
            onConfirmChange(it)
        })
        Spacer(modifier.height(20.dp))
        ConfirmButton("确定", {
            onConfirm()

        })
    }
}

@Composable
fun InputCard(
    title: String,
    hint: String,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }

    Column {
        Text(text = title, color = Color(0xffd2d5da))
        TextField(
            value = password,
            onValueChange = {
                password = it
                onValueChange(password)
            },
            placeholder = {
                Text(hint)
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp), // 文本框本身的形状
            enabled = true,
            maxLines = 1,
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.ic_lock_grey),
                    null,
                    modifier = modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            },
            trailingIcon = {
                Icon(
                    painterResource(if (passwordVisible) R.drawable.login_login_eye_close else R.drawable.login_login_eye_open),
                    null,
                    modifier =
                        modifier
                            .width(24.dp)
                            .height(24.dp)
                            .clickable(onClick = {
                                passwordVisible = !passwordVisible
                            })
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = colorResource(com.zss.base.R.color.gray_9da3ae),
                focusedTextColor = colorResource(com.zss.base.R.color.gray_9da3ae),
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
                focusedIndicatorColor = Color.Transparent, // 隐藏底部指示线
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 14.sp
            ),
            modifier = modifier
                .height(64.dp)
                .padding(top = 8.dp)
                .fillMaxWidth()
                .border(1.dp, Color(0xff373d48), RoundedCornerShape(8.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InfoCardPreview() {
    PswManagerScreen(onPopBack = { true }, navToResult = {})
}

@Composable
fun PswManagerScreen(
    onPopBack: () -> Boolean,
    navToResult: (String) -> Unit,
    modifier: Modifier = Modifier,
    vm: LoginVM = hiltViewModel(),
) {
    FramAistTheme {
        Column(
            modifier = modifier
                .background(Color.Black)
                .padding(horizontal = 28.dp)
                .fillMaxSize()
        ) {
            var currentPsw by remember { mutableStateOf("") }
            var newPsw by remember { mutableStateOf("") }
            var confirmPsw by remember { mutableStateOf("") }
            val state by vm.modifyPswState

            LaunchedEffect(state) {
                if (!state.shouldNavigate) return@LaunchedEffect
                when {
                    state.success -> {
                        LL.e("xdd 修改成功")
                        navToResult.invoke("")
                        vm.clearModifyState()
                    }

                    state.error != null -> {
                        LL.e("xdd 修改失败 $state")
                        navToResult.invoke(state.error ?: "")
                        vm.retryModify()
                    }
                }
            }
            BackTitleCard("密码管理", { onPopBack() })
            Spacer(modifier.height(30.dp))
            ModifyPswCard(onCurChange = {
                currentPsw = it
            }, onNewChange = {
                newPsw = it
            }, onConfirmChange = {
                confirmPsw = it
            }, modifier = modifier, navToResult = navToResult, onConfirm = {
                if (checkPsw(currentPsw, newPsw, confirmPsw)) {
                    vm.modifyPswCompose(currentPsw, newPsw)
                }
            })
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


