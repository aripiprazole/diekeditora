@file:Suppress("EXPERIMENTAL_FEATURE_WARNING", "MemberNameEqualsClassName", "unused")

package com.diekeditora.build

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

val DependencyHandler.kotlin get() = KotlinDependencies(this)
val DependencyHandler.projects get() = ProjectDependencies(this)
val DependencyHandler.libs get() = Libs()

inline class KotlinDependencies(private val handler: DependencyHandler) {
    val stdlibJdk8 get() = handler.kotlin("stdlib-jdk8")
    val reflect get() = handler.kotlin("reflect")
    val testJUnit5 get() = handler.kotlin("test-junit5")
}

inline class ProjectDependencies(private val handler: DependencyHandler) {
    val app get() = handler.project(":app")
    val infra get() = handler.project(":infra")
    val shared get() = handler.project(":shared")
    val domain get() = handler.project(":domain")
}

class Libs {
    val kotlinx = Kotlinx()
    val postgresql = Postgresql()
    val springBoot = SpringBoot()
    val slf4j = Slf4j()
    val reactor = Reactor()
    val expediaGroup = ExpediaGroup()
    val junit = JUnit()
    val pinterest = Pinterest()
    val r2dbc = R2dbc()
    val classgraph = Classgraph()
    val logback = Logback()
    val fusesource = Fusesource()
    val h2database = H2database()
    val serpro69 = Serpro69()
    val springSecurity = SpringSecurity()
    val firebase = Firebase()
    val google = Google()
    val mockk = Mockk()
    val ninjaSquad = NinjaSquad()
    val ozimov = Ozimov()
    val awssdk = Awssdk()
    val valiktor = Valiktor()

    class NinjaSquad {
        val springMockk = "com.ninja-squad:springmockk:3.0.1"
    }

    class Mockk {
        val mockk = "io.mockk:mockk:1.11.0"
    }

    class Google {
        val common = "com.google.api:api-common:1.10.3"
    }

    class Firebase {
        val admin = "com.google.firebase:firebase-admin:7.3.0"
    }

    class Serpro69 {
        val kotlinFaker = "io.github.serpro69:kotlin-faker:1.4.1"
    }

    class H2database {
        val h2 = "com.h2database:h2:1.4.200"
    }

    class Fusesource {
        val jansi = "org.fusesource.jansi:jansi:1.18"
    }

    class Logback {
        val classic = "ch.qos.logback:logback-classic:1.2.3"
    }

    class Pinterest {
        val ktlint = "com.pinterest:ktlint:0.41.0"
    }

    class JUnit {
        val jupiterApi = "org.junit.jupiter:junit-jupiter-api:5.6.0"
        val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:5.6.0"
    }

    class ExpediaGroup {
        val graphQLKotlinSpringServer = "com.expediagroup:graphql-kotlin-spring-server:4.1.0"
    }

    class Reactor {
        val test = "io.projectreactor:reactor-test:3.4.5"
        val kotlin = "io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.3"
        val core = "io.projectreactor:reactor-core:3.4.5"
    }

    class Postgresql {
        val postgresql = "org.postgresql:postgresql:42.2.20"
    }

    class SpringBoot {
        val security = "org.springframework.boot:spring-boot-starter-security:2.4.5"
        val actuator = "org.springframework.boot:spring-boot-starter-actuator"
        val securityOAuth2ResourceServer =
            "org.springframework.boot:spring-boot-starter-oauth2-resource-server"
        val webflux = "org.springframework.boot:spring-boot-starter-webflux:2.4.5"
        val dataR2dbc = "org.springframework.boot:spring-boot-starter-data-r2dbc:2.4.5"
        val dataRedisReactive =
            "org.springframework.boot:spring-boot-starter-data-redis-reactive:2.4.5"
        val test = "org.springframework.boot:spring-boot-starter-test:2.4.5"
        val configurationProcessor = "org.springframework.boot:spring-boot-configuration-processor:2.5.3"
    }

    class SpringSecurity {
        val core = "org.springframework.security:spring-security-oauth2-client:5.5.0"
        val oauth2Client = "org.springframework.security:spring-security-oauth2-client:5.5.0"
        val test = "org.springframework.security:spring-security-test:5.5.0"
    }

    class Slf4j {
        val api = "org.slf4j:slf4j-api:1.7.25"
    }

    class Kotlinx {
        val coroutinesReactor = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.3"
        val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"
        val coroutinesJdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.3"
    }

    class Classgraph {
        val classgraph = "io.github.classgraph:classgraph:4.8.105"
    }

    class R2dbc {
        val h2 = "io.r2dbc:r2dbc-h2:0.8.4.RELEASE"
        val postgresql = "io.r2dbc:r2dbc-postgresql:0.8.7.RELEASE"
    }

    class Ozimov {
        val embeddedRedis = "it.ozimov:embedded-redis:0.7.3"
    }

    class Awssdk {
        val s3 = "software.amazon.awssdk:s3:2.17.3"
    }

    class Valiktor {
        val valiktor = "org.valiktor:valiktor-core:0.12.0"
    }
}
