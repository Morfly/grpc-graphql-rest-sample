import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.protobuf)
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

application {
    mainClass.set("io.morfly.server.ApplicationKt")
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}" }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpcJava.get()}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${libs.versions.grpcKotlin.get()}:jdk7@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

dependencies {

    // ===== kotlin =====
    implementation(kotlin("stdlib"))
    implementation(libs.common.kotlin.datetime)
    implementation(libs.common.kotlin.coroutines)

    // ===== ktor server =====
    implementation(libs.bundles.server.ktor)
    implementation(libs.bundles.server.ktorClient)

    // ===== database =====
    implementation(libs.bundles.server.exposed)
    implementation(libs.server.h2)
    implementation(libs.server.hikaricp)

    // ===== dependency injection =====
    implementation(libs.server.koin)

    // ===== graphql =====
    implementation(libs.bundles.server.graphql)

    // ===== grpc =====
    protobuf(project(":proto"))
    implementation(libs.server.grpc.protobuf)
    implementation(libs.common.grpc.kotlinStub)
    runtimeOnly(libs.server.grpc.netty)

    // ===== tests =====
    testImplementation(libs.server.test.ktor)
}