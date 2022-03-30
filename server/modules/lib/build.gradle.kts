kotlin {
  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(libs.ktor.serialization)
        implementation(libs.ktor.server.jetty)
        implementation("com.expediagroup:graphql-kotlin-spring-server:5.3.2")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
      }
    }
  }
}
