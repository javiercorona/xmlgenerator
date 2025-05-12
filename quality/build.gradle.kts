plugins {
    kotlin("jvm")
}

group = "com.example.mybigapp.quality"
version = "1.0.0"

dependencies {
    implementation(kotlin("stdlib"))
    // later: add lint, accessibility, performance libraries if needed
}
