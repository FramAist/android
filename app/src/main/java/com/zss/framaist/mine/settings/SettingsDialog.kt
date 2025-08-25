package com.zss.framaist.mine.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun SettingDialog(
    onConfirm: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsUiState by viewModel.settingUiState.collectAsStateWithLifecycle()

    SettingDialog(
        onConfirm = onConfirm,
        settingsUiState = settingsUiState,
        onChangeDarkThemeConfig = viewModel::setDarkThemeConfig,
    )
}

@Composable
fun SettingDialog(
    onConfirm: () -> Unit,
    settingsUiState: SettingsUiState,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit
) {

    AlertDialog(
        onDismissRequest = {
            onConfirm
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("确定")
            }
        },
        text = {
            HorizontalDivider()
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                when (settingsUiState) {
                    SettingsUiState.Loading -> {
                        Text("加载中...")
                    }

                    is SettingsUiState.Success -> {
                        SettingsPanel(
                            settings = settingsUiState.settings,
                            onChangeDarkThemeConfig = onChangeDarkThemeConfig
                        )
                    }
                }

            }
        }
    )
}

@Composable
fun SettingsPanel(
    settings: UserEditableSettings,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text("主题")
        ThemeChooseView(
            "跟随系统",
            settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
            onClick = {
                onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM)
            })
        ThemeChooseView("普通模式", settings.darkThemeConfig == DarkThemeConfig.LIGHT, onClick = {
            onChangeDarkThemeConfig(DarkThemeConfig.LIGHT)
        })
        ThemeChooseView("暗黑模式", settings.darkThemeConfig == DarkThemeConfig.DARK, onClick = {
            onChangeDarkThemeConfig(DarkThemeConfig.DARK)
        })
    }
}

@Composable
fun ThemeChooseView(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(text)
    }
}

@Preview
@Composable
private fun SettingDialogPreview() {
    SettingDialog({})
}

enum class DarkThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK
}

