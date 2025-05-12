package com.example.mybigapp.core.layout

import com.parser.ParsedActivity

/**
 * Infers constraint rules (e.g., positioning, margins) for each view
 * in a ConstraintLayout.
 *
 * @param matchConstraints if true, generates "0dp" (match_constraint)
 *                         otherwise uses wrap_content/fixed sizes.
 */
class ConstraintInference {

    /**
     * Generate a list of ConstraintInfo for every view in the layout.
     */
    fun inferConstraints(
        parsed: ParsedActivity,
        matchConstraints: Boolean = false
    ): List<ConstraintInfo> {
        // TODO: implement real inference based on view relationships
        return parsed.views.map { view ->
            ConstraintInfo(
                viewId = view.id,
                attributes = mapOf(
                    "layout_width"  to if (matchConstraints) "0dp" else "wrap_content",
                    "layout_height" to if (matchConstraints) "0dp" else "wrap_content",
                    // stub: you’ll add actual constraint attributes here
                )
            )
        }
    }
}

/**
 * Holds the attributes for a single view’s ConstraintLayout parameters.
 */
data class ConstraintInfo(
    val viewId: String,
    val attributes: Map<String, String>
)
