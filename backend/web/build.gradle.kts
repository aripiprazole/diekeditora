import com.lorenzoog.diekeditora.build.Deps

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
    application
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infra"))
    implementation(project(":shared"))

    runtimeOnly(Deps.PostgreSQL.PostgreSQL)

    implementation(Deps.R2dbc.PostgreSQL)
    implementation(Deps.Classgraph.Classgraph)
    implementation(Deps.ExpediaGroup.GraphQLKotlinSpringServer)
    implementation(Deps.Logback.Classic)
    implementation(Deps.Fusesource.Jansi)

    testRuntimeOnly(Deps.H2Database.H2)
    testImplementation(Deps.R2dbc.H2)
    testImplementation(Deps.Serpro69.KotlinFaker)
}
