import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val arrow_version: String by project
val coroutines_version: String by project

plugins {
  kotlin("jvm") version "1.9.10"
}

group = "de.welcz"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "17"
    languageVersion = "1.9"
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
  implementation("io.arrow-kt:arrow-core:$arrow_version")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  freeCompilerArgs = listOf("-Xinline-classes")
}
