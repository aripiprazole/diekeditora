import com.diekeditora.build.kotlin
import com.diekeditora.build.libs
import com.diekeditora.build.projects

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
    id("composite-build")
    application
}

group = "com.diekeditora"
version = "0.0.1-SNAPSHOT"

dependencies {
    ktlintRuleset(libs.pinterest.ktlint)
    implementation(projects.web)
    implementation(projects.domain)
    implementation(projects.infra)
    implementation(projects.shared)
}

application {
    mainClass.set("com.diekeditora.web.DiekEditoraAppKt")
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "composite-build")

    group = "com.diekeditora"
    version = rootProject.version

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        implementation(kotlin.stdlibJdk8)
        implementation(kotlin.reflect)

        implementation(libs.google.common)

        implementation(libs.firebase.admin)

        implementation(libs.springBoot.dataR2dbc)
        implementation(libs.springBoot.dataRedisReactive)
        implementation(libs.springBoot.webflux)

        implementation(libs.reactor.core)
        implementation(libs.reactor.kotlin)

        implementation(libs.kotlinx.coroutinesCore)
        implementation(libs.kotlinx.coroutinesJdk8)
        implementation(libs.kotlinx.coroutinesReactor)

        implementation(libs.expediaGroup.graphQLKotlinSpringServer)

        implementation(libs.slf4j.api)

        testImplementation(libs.mockk.mockk)
        testImplementation(kotlin.testJUnit5)
        testImplementation(libs.springBoot.test)
        testImplementation(libs.reactor.test)
        testImplementation(libs.junit.jupiterApi)
        testRuntimeOnly(libs.junit.jupiterEngine)
    }

    ktlint {
        android.set(false)
    }

    detekt {
        buildUponDefaultConfig = true
        allRules = false

        config = files("${rootProject.projectDir}/config/detekt.yml")
        baseline = file("${rootProject.projectDir}/config/baseline.xml")

        reports {
            html.enabled = true
            xml.enabled = true
            txt.enabled = true
            sarif.enabled = true
        }
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict", "-Xopt-in=kotlin.RequiresOptIn")
                jvmTarget = "11"
            }
        }

        compileTestKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict", "-Xopt-in=kotlin.RequiresOptIn")
                jvmTarget = "11"
            }
        }

        test {
            useJUnitPlatform()
        }

        build {
            dependsOn(jar, check)
        }

        detekt.configure {
            jvmTarget = "11"
        }
    }
}

tasks {
    bootBuildImage {
        project.findProperty("imageName")?.toString()?.let {
            imageName = it
        }
    }

    wrapper {
        distributionType = Wrapper.DistributionType.ALL
        gradleVersion = "7.0.2"
    }
}
