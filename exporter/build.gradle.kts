plugins {
    // Just a plain JVM library for now
    kotlin("jvm")
}

group = "com.example.mybigapp.exporter"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    // (we’ll add zip & XML libs here as we flesh out FolderExporter & ResourceGenerator)
}
