package com.zss.framaist.mine.settings

import androidx.lifecycle.ViewModel
import com.zss.framaist.util.MMKVUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _settingUiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(
        SettingsUiState.Success(
            settings = UserEditableSettings(
                darkThemeConfig = MMKVUtil.getDarkThemeConfig()
            )
        )
    )
    val settingUiState: StateFlow<SettingsUiState> = _settingUiState.asStateFlow()

    fun setDarkThemeConfig(config: DarkThemeConfig) {
        MMKVUtil.setDarkThemeConfig(config)
        _settingUiState.value = SettingsUiState.Success(
            settings = UserEditableSettings(
                darkThemeConfig = config
            )
        )
    }
}

data class UserEditableSettings(
    val darkThemeConfig: DarkThemeConfig
)

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}