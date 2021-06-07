import com.diekeditora.build.libs
import com.diekeditora.build.projects

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
    application
}

dependencies {
    implementation(projects.domain)
    implementation(projects.infra)
    implementation(projects.shared)

    implementation(libs.exposed.springBootStarter)

    implementation(libs.springBoot.security)
    implementation(libs.springBoot.actuator)
    implementation(libs.springBoot.securityOAuth2ResourceServer)

    runtimeOnly(libs.postgresql.postgresql)

    implementation(libs.classgraph.classgraph)
    implementation(libs.logback.classic)
    implementation(libs.fusesource.jansi)

    testRuntimeOnly(libs.h2database.h2)
    testImplementation(libs.springSecurity.test)
    testImplementation(libs.ninjaSquad.springMockk)
    testImplementation(libs.serpro69.kotlinFaker)
}
