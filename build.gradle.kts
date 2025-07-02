import com.google.protobuf.gradle.id
import org.gradle.kotlin.dsl.proto

plugins {
    kotlin("jvm") version "1.9.21"
    java
    id("com.google.protobuf") version "0.9.4"
    id("com.avast.gradle.docker-compose") version "0.17.12"
    id("org.jreleaser") version "1.12.0"
}

repositories {
    mavenCentral()
}

group = "io.github.kotlinrl"
version = "0.1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.protobuf:protobuf-java:4.28.2")
    implementation("io.grpc:grpc-protobuf:1.60.0")
    implementation("io.grpc:grpc-stub:1.60.0")
    implementation("io.grpc:grpc-kotlin-stub:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    runtimeOnly("io.grpc:grpc-netty:1.60.0")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.14.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest:kotest-property:5.8.0")
}
java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
kotlin {
    jvmToolchain(17)
}
tasks.withType<Test> {
    useJUnitPlatform() // Enable JUnit 5
}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.0" // Protobuf compiler
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.60.0"
        }
        id("grpc-kotlin") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpc-kotlin")
            }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("${rootDir}/protos") // Location of .proto files
        }
    }
}

tasks.named("build") {
    dependsOn("generateProto")
}

dockerCompose {
    useComposeFiles = listOf("docker-compose.yml")
    isRequiredBy(tasks.test)
}