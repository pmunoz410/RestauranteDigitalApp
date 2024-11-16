plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.project.restaurantedigitalapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.restaurantedigitalapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
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
    packaging {
        resources {
            resources {
                excludes.add("META-INF/NOTICE.md")
                excludes.add("META-INF/LICENSE.md")
            }
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Unit testing dependencies
    testImplementation(libs.junit)

    // Android test dependencies
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //----------------- Additional dependencies -----------------//

    // Navigation
    implementation(libs.navigationFragment)
    implementation(libs.navigationUi)

    // Lifecycle components
    implementation(libs.lifecycleLiveDataKtx)
    implementation(libs.lifecycleViewModelKtx)
    // implementation(libs.lifecycleExtensions)
    implementation(libs.lifecycleRuntimeKtx)
    annotationProcessor(libs.lifecycleCompiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofitConverterGson)
    implementation(libs.okhttp3LoggingInterceptor)

    // Dagger
    implementation(libs.daggerAndroid)
    annotationProcessor(libs.daggerAndroidProcessor)

    // Picasso
    implementation(libs.picasso)

    // Stetho
    implementation(libs.stetho)
    implementation(libs.stethoOkhttp3)

    // Material Spinner
    implementation(libs.materialSpinner)

    // JavaMail
    implementation(libs.androidMail)
    implementation(libs.androidActivation)

    // Google Maps
    implementation(libs.googlePlayServicesMaps)
    implementation(libs.googlePlayServicesPlaces)

    // Sweet Alert
    implementation(libs.sweetAlert)

    // Circle ImageView
    implementation(libs.circleImageView)

    // Auto Image Slider
//    implementation(libs.autoImageSlider)
//    implementation ("com.github.smarteist:Android-Image-Slider:1.4.0")
    implementation(libs.slider)

    // Glide
    implementation(libs.glide)
    implementation(libs.glideCompiler)

    // PDF Viewer
//    implementation(libs.androidPdfViewer)
}