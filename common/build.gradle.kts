plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")

}

android {
    namespace = "com.zss.common"
    compileSdk = 35
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = 26
        targetSdk = 34
    }

    buildTypes {
        release {
            buildConfigField("boolean", "RELEASE", "true")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":base"))
}