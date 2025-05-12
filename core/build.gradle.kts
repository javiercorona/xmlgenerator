plugins {
    kotlin("jvm")
}

group = "com.example.mybigapp.core"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("test"))
}
