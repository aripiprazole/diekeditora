buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("jvm") version "1.6.10"
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

dependencies {
}

application {
    mainClass.set("com.diekeditora.DiekEditoraAppKt")
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
