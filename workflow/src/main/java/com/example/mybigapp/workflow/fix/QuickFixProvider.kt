package com.example.mybigapp.workflow.fix

/**
 * Offers one-click fixes for common errors (missing ID,
 * invalid attribute, etc.), driven by JSON rules.
 */
class QuickFixProvider {

    /**
     * @param errorCode the lint or compiler error identifier
     * @return a Kotlin code or XML snippet to apply
     */
    fun getQuickFix(errorCode: String): String? {
        // TODO: load JSON rules and return fix snippet
        return null
    }
}
