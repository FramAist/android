package com.zss.framaist.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zss.framaist.R
import com.zss.framaist.common.showNotSupportedDialog
import com.zss.framaist.compose.ConfirmButton
import com.zss.framaist.compose.ui.theme.FramAistTheme
import com.zss.framaist.util.MMKVUtil

@Composable
fun LoginScreen(vm: LoginVM = hiltViewModel(), onLoginSuccess: () -> Unit) {
    FramAistTheme {
        val loginResult by vm.loginResult.collectAsStateWithLifecycle()
        val loginState by vm.loginSate
        val showNotSupport by vm.showNotSupport.collectAsStateWithLifecycle(false)
        val context = LocalContext.current
        LaunchedEffect(loginResult, showNotSupport) {
            if (loginResult != null) {
                MMKVUtil.setUserInfo(loginResult!!)
                onLoginSuccess()
            }
            if (showNotSupport) {
                showNotSupportedDialog(context)
            }
        }
        LoginPanel(loginState = loginState, onEvent = vm::onEvent)
    }
}

@Composable
fun LoginTabs(
    selectedIndex: Int,
    titles: List<String>,
    onTabSelect: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        indicator = { positions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(positions[selectedIndex]),
                height = 2.dp,
                color = colorResource(com.zss.base.R.color.blue_2a5bff)
            )
        },
        tabs = {
            titles.forEachIndexed { index, title ->
                val isSelected = index == selectedIndex
                val color =
                    if (isSelected) colorResource(com.zss.base.R.color.blue_2a5bff) else colorResource(
                        com.zss.base.R.color.gray_9da3ae
                    )
                Tab(
                    selected = isSelected,
                    onClick = {
                        onTabSelect(index)
                    },
                    modifier = modifier,
                    enabled = true,
                    text = {
                        Box() {
                            Text(
                                text = title,
                                color = color,
                                fontSize = 18.sp
                            )
                        }
                    },
                )
            }
        }
    )
}

@Composable
fun LoginInputCard(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    iconRes: Int,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text // 更灵活的参数
) {
    var textVisible by remember { mutableStateOf(!isPassword) }
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = hint,
                    color = colorResource(com.zss.base.R.color.gray_9da3ae),
                    modifier = Modifier.fillMaxSize()
                )
            },
            singleLine = true,
            visualTransformation = if (textVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp), // 文本框本身的形状
            enabled = true,
            leadingIcon = {
                Icon(
                    painterResource(iconRes),
                    null,
                    tint = colorResource(com.zss.base.R.color.gray_9da3ae),
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            },
            trailingIcon = {
                if (isPassword) {
                    Icon(
                        painterResource(if (textVisible) R.drawable.login_login_eye_close else R.drawable.login_login_eye_open),
                        null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = {
                                textVisible = !textVisible
                            })
                    )
                } else {
                    null
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = colorResource(com.zss.base.R.color.gray_9da3ae),
                focusedTextColor = colorResource(com.zss.base.R.color.gray_9da3ae),
                focusedContainerColor = Color(0xff303030),
                unfocusedContainerColor = Color(0xff303030),
                focusedIndicatorColor = Color.Transparent, // 隐藏底部指示线
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 18.sp
            ),
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun LoginPanel(
    loginState: LoginState,
    onEvent: (LoginEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "欢迎使用 FramAist",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        LoginTabs(loginState.selectedTab, loginState.titles, {
            onEvent(LoginEvent.TabSelected(it))
        })
        Spacer(modifier = Modifier.padding(top = 24.dp))
        LoginInputCard(
            "请输入账号",
            loginState.account,
            onValueChange = {
                onEvent(LoginEvent.AccountChanged(it))
            },
            iconRes = R.drawable.outline_call_24
        )
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            LoginInputCard(
                "请输入密码",
                loginState.password,
                onValueChange = {
                    onEvent(LoginEvent.PasswordChanged(it))
                },
                iconRes = R.drawable.ic_lock_grey,
                isPassword = true
            )
            if (loginState.selectedTab == 1) {
                TextButton(onClick = {
                    onEvent(LoginEvent.GetVerifyCode)
                }) {
                    Text(
                        "获取验证码",
                        color = colorResource(com.zss.base.R.color.blue_2a5bff),
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(top = 12.dp))
        ConfirmButton("登录", {
            onEvent(LoginEvent.HandleLogin)
        })
    }
}

@Preview
@Composable
private fun LoginPreview() {
    LoginScreen(onLoginSuccess = {})
}
