kotlin {
  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(libs.ktor.serialization)
        implementation(libs.ktor.server.jetty)
        implementation(projects.modules.lib)
        implementation("com.expediagroup:graphql-kotlin-spring-server:5.3.2")
      }
    }
  }
}
