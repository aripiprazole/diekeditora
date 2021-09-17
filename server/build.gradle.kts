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
    application
}

group = "com.diekeditora"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    ktlintRuleset(libs.pinterest.ktlint)

    implementation(libs.google.common)

    implementation(libs.firebase.admin)

    implementation(libs.springBoot.dataR2dbc)
    implementation(libs.springBoot.dataRedisReactive)
    implementation(libs.springBoot.webflux)
    implementation(libs.springBoot.security)
    implementation(libs.springBoot.actuator)
    implementation(libs.springBoot.oauth2ResourceServer)

    implementation(libs.springSecurity.oauth2Client)

    implementation(libs.reactor.core)
    implementation(libs.reactor.kotlin)

    implementation(libs.kotlinx.coroutinesCore)
    implementation(libs.kotlinx.coroutinesJdk8)
    implementation(libs.kotlinx.coroutinesReactor)

    implementation(libs.graphQLKotlin.springServer)

    implementation(libs.slf4j.api)
    implementation(libs.valiktor.spring)

    implementation(libs.awssdk.s3)

    runtimeOnly(libs.postgresql)

    implementation(libs.classgraph)
    implementation(libs.r2dbc.postgresql)
    implementation(libs.logback.classic)
    implementation(libs.fusesource.jansi)

    testRuntimeOnly(libs.h2database)
    testImplementation(libs.r2dbc.h2)
    testImplementation(libs.springSecurity.test)
    testImplementation(libs.ninjaSquad.springMockk)
    testImplementation(libs.ozimov.embeddedRedis)
    testImplementation(libs.serpro69.kotlinFaker)

    annotationProcessor(libs.springBoot.configurationProcessor)

    testImplementation(libs.mockk)
    testImplementation(libs.springBoot.test)
    testImplementation(libs.reactor.test)
    testImplementation(libs.junit.jupiterApi)
    testRuntimeOnly(libs.junit.jupiterEngine)

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test-junit5"))
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
