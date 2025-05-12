pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyBigApp"
include(
    ":app",
    ":core",
    ":validator",
    ":exporter",
    ":quality",
    ":workflow",
    ":editor",
    ":custom",
    ":safety"
)

include(":quality:")
include(":workflow:")
include(":editor:")
include(":custom:")
include(":safety:")
