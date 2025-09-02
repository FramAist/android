package com.zss.framaist.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.ToastUtils
import com.zss.base.mvvm.launch
import com.zss.common.bean.LoginResp
import com.zss.framaist.util.MMKVUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 接口少,直接略过repo层,不涉及UI的接口直接使用回调
 */
@HiltViewModel
class LoginVM @Inject constructor(
    private val repo: LoginRepo
) : ViewModel() {

    private val _loginResult: MutableStateFlow<LoginResp?> = MutableStateFlow(null)
    val loginResult: StateFlow<LoginResp?> = _loginResult.asStateFlow()

    private val _showNotSupport: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val showNotSupport: SharedFlow<Boolean> = _showNotSupport.asSharedFlow()

    private val _loginSate = mutableStateOf(LoginState())
    val loginSate: State<LoginState> = _loginSate

    private val _modifyPswState = mutableStateOf(PasswordChangeState())
    val modifyPswState: State<PasswordChangeState> = _modifyPswState

    fun clearModifyState() {
        _modifyPswState.value = PasswordChangeState()
    }

    fun onEvent(loginEvent: LoginEvent) {
        when (loginEvent) {
            is LoginEvent.TabSelected -> {
                _loginSate.value = _loginSate.value.copy(
                    selectedTab = loginEvent.index,
                    password = ""
                )
            }

            is LoginEvent.AccountChanged -> {
                _loginSate.value = _loginSate.value.copy(
                    account = loginEvent.value
                )
            }

            is LoginEvent.PasswordChanged -> {
                _loginSate.value = _loginSate.value.copy(
                    password = loginEvent.value
                )
            }

            LoginEvent.GetVerifyCode -> {
                launch({
                    _showNotSupport.emit(true)
                })
            }

            LoginEvent.HandleLogin -> {
                loginD()
            }
        }
    }

    fun loginD() {
        launch({
            val phone = _loginSate.value.account
            val psw = _loginSate.value.password
            val isPsw = _loginSate.value.selectedTab == 0
            require(phone.isNotEmpty()) { "账号不能为空" }
            require(psw.isNotEmpty()) {
                if (isPsw) {
                    "密码不能为空"
                } else {
                    "验证码不能为空"
                }
            }
            repo.loginRemote(phone, psw, isPsw)
        }, {
            ToastUtils.showShort(it)
        })

    }

    fun modifyPswCompose(oldPsw: String, newPsw: String) {
        launch({
            _modifyPswState.value = _modifyPswState.value.copy(isLoading = true)
            repo.modifyPswRemote(oldPsw, newPsw)
            _modifyPswState.value =
                _modifyPswState.value.copy(success = true, message = "密码修改成功")
        }, {
            _modifyPswState.value =
                _modifyPswState.value.copy(success = false, error = it)
        })
    }
}

data class PasswordChangeState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

data class LoginState(
    val selectedTab: Int = 0,
    val account: String = MMKVUtil.getLastLoginAccount() ?: "",
    val password: String = "",
    val titles: List<String> = listOf("密码登录", "验证码登录")
)

sealed interface LoginEvent {
    data class TabSelected(val index: Int) : LoginEvent
    data class AccountChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data object GetVerifyCode : LoginEvent
    data object HandleLogin : LoginEvent
}