buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
}

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.spring") version "1.4.31"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
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
    ktlintRuleset("com.pinterest:ktlint:0.41.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.31")

    implementation("io.springfox:springfox-boot-starter:3.0.0")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation("ch.qos.logback:logback-classic")
    implementation("org.fusesource.jansi:jansi:1.18")

    implementation("io.r2dbc:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("io.r2dbc:r2dbc-h2")
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
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
        kotlinOptions.jvmTarget = "11"
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
