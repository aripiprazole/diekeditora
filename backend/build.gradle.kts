import com.lorenzoog.diekeditora.build.Deps

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
}

group = "com.lorenzoog"
version = "0.0.1-SNAPSHOT"

dependencies {
    ktlintRuleset(Deps.Pinterest.Ktlint)
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "composite-build")

    group = "com.lorenzoog.diekeditora"
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
        implementation(Deps.Kotlin.Stdlib)
        implementation(Deps.Kotlin.Reflect)

        implementation(Deps.SpringBoot.DataR2dbc)
        implementation(Deps.SpringBoot.Webflux)

        implementation(Deps.Reactor.Core)
        implementation(Deps.Reactor.Kotlin)

        implementation(Deps.Kotlinx.SerializationJson)
        implementation(Deps.Kotlinx.CoroutinesCore)
        implementation(Deps.Kotlinx.CoroutinesJdk8)
        implementation(Deps.Kotlinx.CoroutinesReactor)

        implementation(Deps.Slf4j.Api)

        testImplementation(Deps.Kotlin.TestJUnit5)
        testImplementation(Deps.SpringBoot.Test)
        testImplementation(Deps.Reactor.Test)
        testImplementation(Deps.AxonFramework.Test)
        testImplementation(Deps.JUnit.JupiterApi)
        testRuntimeOnly(Deps.JUnit.JupiterEngine)
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
