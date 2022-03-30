plugins {
  application
}

application {
  mainClass.set("com.diekeditora.DiekEditoraKt")
}

kotlin {
  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(projects.modules.logging)
        implementation(projects.modules.lib)
        implementation(projects.modules.graphql)
        implementation(projects.modules.user)
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(kotlin("test-junit5"))
      }
    }
  }
}
