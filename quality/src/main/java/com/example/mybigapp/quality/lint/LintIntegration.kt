package com.example.mybigapp.quality.lint

/**
 * Integrates custom offline lint rules and flags unsafe practices.
 */
class LintIntegration {

    /**
     * Analyze source files and return a list of lint warnings.
     */
    fun runLint(sourceDirs: List<String>): List<String> {
        // TODO: hook into detekt or custom lint engine
        return emptyList()
    }
}
