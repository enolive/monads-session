import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val arrowVersion: String by project
val coroutinesVersion: String by project

plugins {
  kotlin("jvm") version "2.1.20"
}

group = "de.welcz"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

tasks.withType<KotlinCompile> {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_21
    languageVersion = KotlinVersion.KOTLIN_2_1
    freeCompilerArgs.addAll("-Xinline-classes")
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
  implementation("io.arrow-kt:arrow-core:$arrowVersion")
}

