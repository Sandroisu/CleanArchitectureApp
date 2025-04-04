plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "dev.sandroisu.newssearchapp"
    compileSdk = libs.versions.androidsdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidsdk.min.get().toInt()

        targetSdk = libs.versions.androidsdk.target.get().toInt()
        applicationId = "dev.sandroisu.newssearchapp"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "NEWS_API_KEY", "\"9345bb5f947c4371b232d34605cd65b7\"")
        buildConfigField("String", "NEWS_API_BASE_URL", "\"https://newsapi.org/v2/\"")

        resourceConfigurations += setOf("ru", "en")

        ndk {
            //noinspection ChromeOsAbiSupport
            abiFilters += setOf("armeabi-v7a", "arm64-v8a")
        }
    }

    signingConfigs {
        create("release") {
            storeFile = File(rootDir, "newsapp.keystore")
            keyPassword = "123456"
            keyAlias = "sandroisu"
            storePassword = "123456"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/okhttp3/internal/publicsuffix/NOTICE"
            excludes += "/kotlin/**"
            excludes += "META-INF/androidx.*.version"
            excludes += "META-INF/com.google.*.version"
            excludes += "META-INF/kotlinx_.*.version"
            excludes += "kotlin-tooling-metadata.json"
            excludes += "DebugProbesKt.bin"
            excludes += "META-INF/com/android/build/gradle/app-metadata.properties"
        }
    }
}

dependencies {
    implementation(libs.android.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.profileinstaller)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(projects.newsApi)
    implementation(projects.newsData)
    implementation(projects.features.newsMain.ui)
    implementation(projects.features.newsMain.domain)
    implementation(projects.database)
    implementation(projects.newsCommon)
    implementation(projects.newsUikit)

    debugImplementation(libs.okhttp.logging)
    baselineProfile(projects.baselineprofile)
}
