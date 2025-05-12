package com.example.mybigapp.validator.execution.dependency

/**
 * Inspects build.gradle files to warn about missing dependencies
 * for referenced classes or libraries.
 */
class DependencyChecker {

    /**
     * @return missing dependency coordinates, e.g. "com.google.code.gson:gson:2.8.9"
     */
    fun check(buildGradle: String, referencedClasses: List<String>): List<String> {
        // TODO: parse build.gradle and compare to needed artifacts
        return emptyList()
    }
}
