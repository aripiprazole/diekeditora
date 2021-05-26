import com.lorenzoog.diekeditora.build.libs
import com.lorenzoog.diekeditora.build.projects

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.shared)

    implementation(libs.springBoot.dataR2dbc)
}
