plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.4.21"
    id ("kotlin-parcelize")
}

android {
    namespace = "in.slanglabs.convaai.pg"
    compileSdk = 34

    defaultConfig {
        applicationId = "in.slanglabs.convaai.pg"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

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
            val capabilities = listOf("none","default", "joke", "ninja", "recipe", "grammar")
            val assistantCapabilities = capabilities.joinToString(separator = ",")

            val capabilityGroup = listOf("none","default", "Group 1", "Group 2", "Group 3", "Group 4")
            val assistantCapabilityGroup = capabilityGroup.joinToString(separator = ",")

            buildConfigField("String", "ASSISTANT_CAPABILITIES", "\"$assistantCapabilities\"")
            buildConfigField("String", "ASSISTANT_CAPABILITY_GROUP", "\"$assistantCapabilityGroup\"")
            buildConfigField("String", "ASSISTANT_ID", "\"8bdcfb98d787496e82f2e48d53edfa19\"")
            buildConfigField("String", "API_KEY", "\"2c87766dea05467fad5a04dab6f9f08c\"")
            buildConfigField("String", "ASSISTANT_VERSION", "\"1.0.1\"")
            buildConfigField("boolean", "SKIP_ASSISTANT_SELECTION", "false")
            buildConfigField("String", "ASR_SERVICE_HOST", "\"omni-asr.slanglabs.in\"")
            buildConfigField("String", "ASR_SERVICE_PROTOCOL", "\"wss\"")
            buildConfigField("String", "ASR_SERVICE_VERSION", "\"v1\"")
        }
        debug {
            val capabilities = listOf("none","default", "joke", "ninja", "recipe", "grammar")
            val assistantCapabilities = capabilities.joinToString(separator = ",")

            val capabilityGroup = listOf("none","default", "Group 1", "Group 2", "Group 3", "Group 4")
            val assistantCapabilityGroup = capabilityGroup.joinToString(separator = ",")

            buildConfigField("String", "ASSISTANT_CAPABILITIES", "\"$assistantCapabilities\"")
            buildConfigField("String", "ASSISTANT_CAPABILITY_GROUP", "\"$assistantCapabilityGroup\"")
            buildConfigField("String", "ASSISTANT_ID", "\"6e0f366d98f048778f6f8f01ff01ea2a\"")
            buildConfigField("String", "API_KEY", "\"ee5b3078a8f843f79ae13ca9a3f98334\"")
            buildConfigField("String", "ASSISTANT_VERSION", "\"1.0.0\"")
            buildConfigField("boolean", "SKIP_ASSISTANT_SELECTION", "false")
            buildConfigField("String", "ASR_SERVICE_HOST", "\"omni-asr-stage-xzevyhdsoq-el.a.run.app\"")
            buildConfigField("String", "ASR_SERVICE_PROTOCOL", "\"wss\"")
            buildConfigField("String", "ASR_SERVICE_VERSION", "\"v1\"")
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
    implementation("androidx.compose.foundation:foundation-android:1.6.7")

    // Slang implementations
    implementation ("in.slanglabs.conva:conva-ai-copilot:2.3.1-beta")

    implementation("androidx.camera:camera-camera2:1.2.2")
    implementation("androidx.camera:camera-lifecycle:1.2.2")
    implementation("androidx.camera:camera-view:1.2.2")
    implementation("com.google.mlkit:barcode-scanning:17.1.0")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("com.google.accompanist:accompanist-permissions:0.19.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.google.code.gson:gson:2.8.8")

}