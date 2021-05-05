package com.lorenzoog.diekeditora.build

object Deps {
    object SpringBoot {
        const val Webflux = "org.springframework.boot:spring-boot-starter-webflux:2.4.5"
        const val DataR2dbc = "org.springframework.boot:spring-boot-starter-data-r2dbc:2.4.5"
        const val Test = "org.springframework.boot:spring-boot-starter-test:2.4.5"
    }

    object Kotlin {
        const val Stdlib = "org.jetbrains.kotlin:kotlin-reflect:1.5.0"
        const val Reflect = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.0"
    }

    object Kotlinx {
        const val CoroutinesReactor = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.0-RC"
        const val CoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-RC"
        const val CoroutinesJdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.5.0-RC"
        const val SerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.0"
    }

    object Slf4j {
        const val Api = "org.slf4j:slf4j-api:1.7.25"
    }

    object PostgreSQL {
        const val PostgreSQL = "org.postgresql:postgresql:42.2.20"
    }

    object R2dbc {
        const val H2 = "io.r2dbc:r2dbc-h2:0.8.4.RELEASE"
        const val PostgreSQL = "io.r2dbc:r2dbc-postgresql:0.8.7.RELEASE"
    }

    object Fusesource {
        const val Jansi = "org.fusesource.jansi:jansi:1.18"
    }

    object Logback {
        const val Classic = "ch.qos.logback:logback-classic:1.2.3"
    }

    object Springfox {
        const val Starter = "io.springfox:springfox-boot-starter:3.0.0"
    }

    object ExpediaGroup {
        const val GraphQLKotlinSpringServer = "com.expediagroup:graphql-kotlin-spring-server:4.1.0"
    }

    object Classgraph {
        const val Classgraph = "io.github.classgraph:classgraph:4.8.105"
    }

    object AxonFramework {
        const val Core = "org.axonframework:axon-core:4.0-M2"
        const val Test = "org.axonframework:axon-test:4.5"
        const val KotlinExtension = "org.axonframework.extensions.kotlin:axon-kotlin:0.1.0"
    }

    object Reactor {
        const val Test = "io.projectreactor:reactor-test:3.4.5"
        const val Kotlin = "io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.3"
        const val Core = "io.projectreactor:reactor-core:3.4.5"
    }

    object Pinterest {
        const val Ktlint = "com.pinterest:ktlint:0.41.0"
    }

    object JUnit {
        const val JupiterApi = "org.junit.jupiter:junit-jupiter-api:5.6.0"
        const val JupiterEngine = "org.junit.jupiter:junit-jupiter-engine:5.6.0"
    }

    object Serpro69 {
        const val KotlinFaker = "io.github.serpro69:kotlin-faker:1.4.1"
    }

    object H2Database {
        const val H2 = "com.h2database:h2:1.4.200"
    }
}
