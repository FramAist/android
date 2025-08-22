// Top-level build file where you can add configuration options common to all sub-projects/modules.
// 顶层文件申明插件, apply false表示不立即启用. 申明后各个module可以通过Alisa来调用.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}