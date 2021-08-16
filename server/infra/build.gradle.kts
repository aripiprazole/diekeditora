import com.diekeditora.build.libs
import com.diekeditora.build.projects

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.shared)

    implementation(libs.awssdk.s3)

    implementation(libs.springBoot.security)
    implementation(libs.springBoot.dataR2dbc)
    implementation(libs.springSecurity.oauth2Client)

    annotationProcessor(libs.springBoot.configurationProcessor)
}
