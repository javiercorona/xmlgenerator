package com.generator

import com.example.mybigapp.core.layout.ConstraintInference
import com.example.mybigapp.core.layout.ConstraintInfo
import com.parser.ParsedActivity

/**
 * Converts a ParsedActivity model into an Android XML layout string.
 */
class XmlGenerator {
    private val constraintInfer = ConstraintInference()

    /**
     * Generate an XML layout string for the given activity.
     */
    fun generateLayoutXml(
        activity: ParsedActivity,
        prettify: Boolean = true
    ): String {
        // Always use ConstraintLayout for demo clarity
        val rootTag = "androidx.constraintlayout.widget.ConstraintLayout"

        // infer constraints
        val constraints: List<ConstraintInfo> = constraintInfer.inferConstraints(activity)

        // build XML
        val sb = StringBuilder()
        sb.append("""<?xml version=\"1.0\" encoding=\"utf-8\"?>""").append("\n")
        sb.append("<$rootTag\n")
            .append("    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n")
            .append("    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n")
            .append("    android:layout_width=\"match_parent\"\n")
            .append("    android:layout_height=\"match_parent\">\n")

        for (view in activity.views) {
            val ci = constraints.firstOrNull { it.viewId == view.id }
            val w = ci?.attributes?.get("layout_width")  ?: "wrap_content"
            val h = ci?.attributes?.get("layout_height") ?: "wrap_content"

            sb.append("    <${view.type}\n")
                .append("        android:id=\"@+id/${view.id}\"\n")
                .append("        android:layout_width=\"$w\"\n")
                .append("        android:layout_height=\"$h\"")

            activity.clickHandlers.find { it.viewId == view.id }?.let { click ->
                sb.append("\n        android:onClick=\"${click.methodName}\"")
            }

            ci?.attributes?.forEach { (attr, value) ->
                if (attr.startsWith("constraint")) {
                    val xmlAttr = "app:layout_" + attr.removePrefix("constraint")
                    sb.append("\n        $xmlAttr=\"$value\"")
                }
            }
            sb.append(" />\n")
        }

        sb.append("</$rootTag>\n")
        return if (prettify) prettifyXml(sb.toString()) else sb.toString()
    }

    private fun prettifyXml(xml: String): String = xml.lines()
        .joinToString("\n") { it.trimEnd() }
}

