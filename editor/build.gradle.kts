plugins {
    kotlin("jvm")
}

group = "com.example.mybigapp.editor"
version = "1.0.0"

dependencies {
    implementation(kotlin("stdlib"))
    // later: add swing/JavaFX or Compose Desktop if needed for drag & drop UI
}
