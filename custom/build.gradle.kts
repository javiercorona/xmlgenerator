plugins {
    kotlin("jvm")
}

group = "com.example.mybigapp.custom"
version = "1.0.0"

dependencies {
    implementation(kotlin("stdlib"))
    // later: add kotlinx-serialization, JSON libs, or templating engines
}
