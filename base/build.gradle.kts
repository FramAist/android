plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    //alias(libs.plugins.ksp)
}

android {
    namespace = "com.zss.base"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testOptions.targetSdk = 36
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
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    testApi(libs.androidx.junit)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.activity)
    api(libs.third.permissionx)
    api(libs.androidx.camera.core)
    api(libs.androidx.camera.lifecycle)
    api(libs.androidx.camera.view)
    api(libs.androidx.camera.camera2)
    api(libs.androidx.camera.extensions)
    //api(libs.androidx.camera.video)
    api(libs.androidx.camera.mlkit.vision)

    api(libs.third.autosize)
    api(libs.third.liveEventBus)
    api(libs.third.immersionbar)
    api(libs.third.immersionbar.ktx)
    api(libs.third.mmkv)
    api(libs.third.okhttp)
//    ksp(libs.third.theRouter.apt)
//    api(libs.third.theRouter.router)
    api(libs.third.shapeview)
    api(libs.third.shape.drawable)
    api(libs.third.glide)
    api(libs.third.glide.transformations)
    api(libs.third.gson.factory)
    api(libs.third.convert.gson)
    api(libs.third.angcyo.dsltablayout.tablayout)
    api(libs.third.angcyo.dsltablayout.viewpager2delegate)
    api(libs.third.baseRecyclerViewAdapterHelper)
    api(libs.third.xpop)
    api(libs.third.lucksiege.picture.selector)
    api(libs.third.round.corner.progress.bar)
    api(libs.third.utilcodex)
    api(libs.third.spin.kit)
    api(libs.androidx.fragment.ktx)
    api(libs.databinding.viewbinding)
}