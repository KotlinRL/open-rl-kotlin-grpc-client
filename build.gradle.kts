import com.google.protobuf.gradle.id
import org.gradle.kotlin.dsl.dockerCompose

plugins {
    kotlin("jvm") version "1.9.21"
    java
    id("com.google.protobuf") version "0.9.4"
    id("com.avast.gradle.docker-compose") version "0.17.12"
    id("maven-publish")
    id("signing")
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
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "io.github.kotlinrl"
            artifactId = "open-rl-kotlin-grpc-client"
            version = project.version.toString()
            pom {
                name.set("Open RL Kotlin gRPC Client")
                description.set("A Kotlin client for Open RL Env Servers")
                url.set("https://github.com/KotlinRL/open-rl-kotlin-grpc-client")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
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
                    connection.set("scm:git:https://github.com/KotlinRL/open-rl-kotlin-grpc-client.git")
                    developerConnection.set("scm:git:ssh://git@github.com:KotlinRL/open-rl-kotlin-grpc-client.git")
                    url.set("https://github.com/KotlinRL/open-rl-kotlin-grpc-client")
                }
            }
        }
    }
    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = findProperty("OSSRH_USERNAME") as String?
                password = findProperty("OSSRH_PASSWORD") as String?
            }
        }
        maven {
            name = "OSSRH-Snapshots"
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            credentials {
                username = findProperty("OSSRH_USERNAME") as String?
                password = findProperty("OSSRH_PASSWORD") as String?
            }
        }
    }
}
signing {
    val signingKey: String? = findProperty("SIGNING_SECRET_KEY") as String?
    val signingPassword: String? = findProperty("SIGNING_PASSWORD") as String?

    // Only configure signing if key and password are available
    isRequired = signingKey != null && signingPassword != null

    if (isRequired) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["mavenJava"])
    }
}