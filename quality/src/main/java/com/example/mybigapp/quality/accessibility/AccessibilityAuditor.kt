package com.example.mybigapp.quality.accessibility

/**
 * Audits layouts for accessibility issues: missing contentDescription,
 * small touch targets, contrast problems.
 */
class AccessibilityAuditor {

    /**
     * @return a list of accessibility issues found
     */
    fun audit(layoutXmls: List<String>): List<String> {
        // TODO: parse XML and apply rules
        return emptyList()
    }
}
