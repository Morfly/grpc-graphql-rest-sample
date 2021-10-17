import com.google.protobuf.gradle.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.apollo)
    alias(libs.plugins.protobuf)
}

apollo {
    packageName.set("io.morfly.client")
    generateKotlinModels.set(true)
    customTypeMapping.set(
        mapOf(
            "Timestamp" to "kotlinx.datetime.Instant"
        )
    )
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }
    plugins {
        id("java") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpcJava.get()}"
        }
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpcJava.get()}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${libs.versions.grpcKotlin.get()}:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("java") {
                    option("lite")
                }
                id("grpc") {
                    option("lite")
                }
                id("grpckt") {
                    option("lite")
                }
            }
        }
    }
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "io.morfly.client"
        minSdk = 21
        targetSdk = 30
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {

    // ===== kotlin =====
    implementation(libs.common.kotlin.datetime)
    implementation(libs.common.kotlin.coroutines)
    implementation(libs.android.kotlin.coroutines)
    implementation(libs.android.kotlin.serialization)

    // ===== android =====
    implementation(libs.bundles.android.core)

    // ===== compose =====
    implementation(libs.bundles.android.compose)
    implementation(libs.android.compose.navigation)
    debugImplementation(libs.android.compose.uiTooling)

    // ===== rest =====
    implementation(libs.bundles.android.retrofit)

    // ===== graphql =====
    implementation(libs.bundles.android.apollo)

    // ===== grpc ======
    protobuf(project(":proto"))
    implementation(libs.android.grpc.protobufLite)
    implementation(libs.android.protobufLite)
    implementation(libs.common.grpc.kotlinStub)
    runtimeOnly(libs.android.grpc.okhttp)

    // ===== dependency injection
    implementation(libs.bundles.android.koin)

//    api("io.grpc:grpc-protobuf-lite:1.37.0")
//    api("io.grpc:grpc-kotlin-stub:1.1.0")
//    api("com.google.protobuf:protobuf-javalite:3.17.2")
}