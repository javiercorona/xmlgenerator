package com.example.mybigapp.core.layout

import com.parser.ParsedActivity

/**
 * Automatically chooses a layout container (ConstraintLayout, LinearLayout, FrameLayout)
 * based on the ParsedActivity model.
 *
 * TODO:
 *  • implement rule-based selection (via JSON config)
 *  • score different layouts (nesting, number of views, complexity)
 */
class LayoutTypeDetector {

    /**
     * Suggests the best root layout for this activity.
     * @param parsed ParsedActivity with view info
     * @return the XML tag name, e.g. "androidx.constraintlayout.widget.ConstraintLayout"
     */
    fun detectRootLayout(parsed: ParsedActivity): String {
        // Simple heuristic stub:
        return if (parsed.views.size > 5) {
            "androidx.constraintlayout.widget.ConstraintLayout"
        } else {
            "android.widget.LinearLayout"
        }
    }
}
