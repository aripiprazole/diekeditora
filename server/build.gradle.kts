@file:Suppress("UnusedPrivateMember")

import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
  kotlin("multiplatform") version "1.6.10"
  kotlin("plugin.serialization") version "1.6.10"
  id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
  id("io.gitlab.arturbosch.detekt") version "1.19.0"
  application
}

group = "com.diekeditora"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()
}

application {
  mainClass.set("com.diekeditora.DiekEditoraKt")
}

kotlin {
  jvm {
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
      testLogging.showStandardStreams = true
      testLogging.exceptionFormat = TestExceptionFormat.FULL
    }
  }

  sourceSets {
    all {
      languageSettings {
        optIn("kotlin.RequiresOptIn")
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation(libs.jansi)
        implementation(libs.logback.classic)
        implementation(libs.slf4j.api)
        implementation(libs.ktor.serialization)
        implementation(libs.ktor.server.jetty)
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.3.2")
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(kotlin("test-junit5"))
      }
    }
  }
}

ktlint {
  android.set(false)
}

detekt {
  buildUponDefaultConfig = true
  allRules = false

  config = files("${rootProject.projectDir}/config/detekt.yml")
  baseline = file("${rootProject.projectDir}/config/baseline.xml")
}

tasks {
  build {
    dependsOn(jar, check)
  }

  detekt.configure {
    jvmTarget = "11"
  }

  wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.4.1"
  }
}
