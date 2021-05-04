import com.lorenzoog.diekeditora.build.Deps

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
}

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
    id("composite-build")
    application
}

group = "com.lorenzoog"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    ktlintRuleset(Deps.Pinterest.Ktlint)

    implementation(Deps.Kotlin.Stdlib)
    implementation(Deps.Kotlin.Reflect)

    implementation(Deps.SpringBoot.DataR2dbc)
    implementation(Deps.SpringBoot.Webflux)
    implementation(Deps.Springfox.Starter)

    implementation(Deps.ExpediaGroup.GraphQLKotlinSpringServer)

    implementation(Deps.Classgraph.Classgraph)

    implementation(Deps.Reactor.Core)
    implementation(Deps.Reactor.Kotlin)

    implementation(Deps.Kotlinx.SerializationJson)
    implementation(Deps.Kotlinx.CoroutinesCore)
    implementation(Deps.Kotlinx.CoroutinesJdk8)
    implementation(Deps.Kotlinx.CoroutinesReactor)

    implementation(Deps.Slf4j.Api)
    implementation(Deps.Logback.Classic)
    implementation(Deps.Fusesource.Jansi)

    implementation(Deps.R2dbc.PostgreSQL)
    runtimeOnly(Deps.PostgreSQL.PostgreSQL)

    testImplementation(Deps.SpringBoot.Test)
    testImplementation(Deps.Reactor.Test)

    testImplementation(Deps.JUnit.JupiterApi)
    testRuntimeOnly(Deps.JUnit.JupiterEngine)

    testImplementation(Deps.Serpro69.KotlinFaker)

    testRuntimeOnly(Deps.H2Database.H2)
    testImplementation(Deps.R2dbc.H2)
}

ktlint {
    android.set(false)
}

detekt {
    buildUponDefaultConfig = true
    allRules = false

    config = files("$projectDir/config/detekt.yml")
    baseline = file("$projectDir/config/baseline.xml")

    reports {
        html.enabled = true
        xml.enabled = true
        txt.enabled = true
        sarif.enabled = true
    }
}

application {
    mainClass.set("com.lorenzoog.diekeditora.DiekEditoraAppKt")
}

tasks {
    compileKotlin {
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict", "-Xopt-in=kotlin.RequiresOptIn")
        kotlinOptions.jvmTarget = "11"
    }

    compileTestKotlin {
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict", "-Xopt-in=kotlin.RequiresOptIn")
        kotlinOptions.jvmTarget = "11"
    }

    test {
        useJUnitPlatform()
    }

    jar {
        dependsOn(check)
    }

    build {
        dependsOn(jar)
    }

    detekt.configure {
        jvmTarget = "11"
    }
}
