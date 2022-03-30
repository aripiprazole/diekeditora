kotlin {
  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(libs.jansi)
        implementation(libs.logback.classic)
        implementation(libs.slf4j.api)
      }
    }
  }
}
