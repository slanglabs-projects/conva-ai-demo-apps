plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "in.slanglabs.androidpg"
    compileSdk = 34

    defaultConfig {
        applicationId = "in.slanglabs.androidpg"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val capabilities = listOf("Default", "Joke Generator", "Grammar Ninja", "Recipe Generator")
            val assistantCapabilities = capabilities.joinToString(separator = ",")

            buildConfigField("String", "ASSISTANT_CAPABILITIES", "\"$assistantCapabilities\"")
            buildConfigField("String", "ASSISTANT_ID", "\"8bdcfb98d787496e82f2e48d53edfa19\"")
            buildConfigField("String", "API_KEY", "\"2c87766dea05467fad5a04dab6f9f08c\"")
            buildConfigField("String", "ASSISTANT_VERSION", "\"1.0.1\"")
        }
        debug {
            val capabilities = listOf("Default", "Joke Generator", "Grammar Ninja", "Recipe Generator")
            val assistantCapabilities = capabilities.joinToString(separator = ",")

            buildConfigField("String", "ASSISTANT_CAPABILITIES", "\"$assistantCapabilities\"")
            buildConfigField("String", "ASSISTANT_ID", "\"8bdcfb98d787496e82f2e48d53edfa19\"")
            buildConfigField("String", "API_KEY", "\"2c87766dea05467fad5a04dab6f9f08c\"")
            buildConfigField("String", "ASSISTANT_VERSION", "\"1.0.1\"")
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.compose.foundation:foundation-android:1.6.6")
    implementation ("in.slanglabs.conva:conva-ai-core:0.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}