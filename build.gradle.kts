import com.google.protobuf.gradle.id
import org.gradle.kotlin.dsl.proto

plugins {
    kotlin("jvm") version "1.9.21"
    id("com.google.protobuf") version "0.9.4"
    id("com.avast.gradle.docker-compose") version "0.17.12"
    id("com.vanniktech.maven.publish") version "0.33.0"
}

repositories {
    mavenCentral()
}

group = "io.github.kotlinrl"
version = "0.1.0-SNAPSHOT"
description = "Kotlin gRPC + Protobuf Client library for Reinforcement Learning with Open RL gRPC Servers"

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

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), project.name, version.toString())

    pom {
        name.set("Open RL Kotlin gRPC Client")
        description.set(project.description)
        url.set("https://github.com/kotlinrl/open-rl-kotlin-grpc-client")
        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("dkrieg")
                name.set("Daniel Krieg")
                email.set("daniel_krieg@mac.com")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/kotlinrl/open-rl-kotlin-grpc-client.git")
            developerConnection.set("scm:git:ssh://github.com:kotlinrl/open-rl-kotlin-grpc-client.git")
            url.set("https://github.com/kotlinrl/open-rl-kotlin-grpc-client")
        }
    }
}