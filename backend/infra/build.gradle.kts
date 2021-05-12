import com.lorenzoog.diekeditora.build.Deps

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":shared"))

    implementation(Deps.SpringBoot.DataR2dbc)
}
