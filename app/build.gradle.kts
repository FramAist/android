import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    //id("therouter")
    id("kotlin-parcelize")
}

android {
    namespace = "com.zss.framaist"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.zss.framaist"
        minSdk = 28
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a"))
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("/Users/zhanghui/Desktop/FramAist2/framasit.jks")
            keyAlias = "key0"
            storePassword = "fa8888"
            keyPassword = "fa8888"
            enableV1Signing = true
            enableV2Signing = true
        }
    }


    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
//    externalNativeBuild {
//        cmake {
//            // 指定 CMakeLists.txt 文件的路径
//            path(file("src/main/cpp/CMakeLists.txt"))
//        }
//    }
    ndkVersion = "29.0.13113456 rc1"

    lint {
        abortOnError = false
        checkGeneratedSources = true
    }

    applicationVariants.all {
        outputs.forEach {
            val timeNow = SimpleDateFormat("yyyyMMdd").format(Date())
            val newName = "framasit_${buildType.name}_${versionCode}_${timeNow}.apk"
            (it as BaseVariantOutputImpl).outputFileName = newName
        }
    }

}

dependencies {
    implementation(project(":common"))
    implementation(project(":base"))
    //ksp(libs.third.theRouter.apt)
    //implementation(libs.third.theRouter.router)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
}

