plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

gradlePlugin {
    plugins.register("composite-build") {
        id = "composite-build"
        implementationClass = "com.diekeditora.build.BuildSrc"
    }
}