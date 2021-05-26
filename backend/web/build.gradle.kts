import com.lorenzoog.diekeditora.build.libs
import com.lorenzoog.diekeditora.build.projects

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
    application
}

dependencies {
    implementation(projects.domain)
    implementation(projects.infra)
    implementation(projects.shared)

    implementation(libs.springBoot.security)

    runtimeOnly(libs.postgresql.postgresql)

    implementation(libs.r2dbc.postgresql)
    implementation(libs.classgraph.classgraph)
    implementation(libs.logback.classic)
    implementation(libs.fusesource.jansi)

    testRuntimeOnly(libs.h2database.h2)
    testImplementation(libs.r2dbc.h2)
    testImplementation(libs.serpro69.kotlinFaker)
}
