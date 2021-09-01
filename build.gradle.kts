import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.20"
}

group = "com.kriptal"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api("org.slf4j:slf4j-api:1.7.32")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.0")
    testImplementation("io.kotest:kotest-property-jvm:4.6.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test> {
    useJUnitPlatform()
}