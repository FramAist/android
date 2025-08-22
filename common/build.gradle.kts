plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
}

android {
    namespace = "com.zss.common"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testOptions.targetSdk = 36
    }

    buildTypes {
        release {
            buildConfigField("boolean", "RELEASE", "true")
            //单独编译时不混淆,如果没有consumerProguardFiles,还是会被主module混淆.
            isMinifyEnabled = false
            //保护本module的混淆规则不受主module影响.
            consumerProguardFiles("proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":base"))
    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
    testApi(libs.androidx.junit)
}