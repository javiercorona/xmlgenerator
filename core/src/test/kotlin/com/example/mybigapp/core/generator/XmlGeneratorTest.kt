package com.example.mybigapp.core.generator

import com.example.mybigapp.core.layout.ConstraintInference
import com.example.mybigapp.core.layout.LayoutTypeDetector
import com.parser.ParsedActivity

/**
 * Converts a ParsedActivity model into an Android XML layout string.
 */
class XmlGenerator {
    private val layoutDetector = LayoutTypeDetector()
    private val constraintInfer = ConstraintInference()

    fun generateLayoutXml(
        activity: ParsedActivity,
        prettify: Boolean = true
    ): String {
        // 1) choose root layout
        val rootTag = layoutDetector.detectRootLayout(activity)
        // 2) infer constraints (for ConstraintLayout only)
        val constraints = if (rootTag.endsWith("ConstraintLayout"))
            constraintInfer.inferConstraints(activity)
        else
            emptyList()

        // build XML
        val sb = StringBuilder()
        sb.append("""<?xml version="1.0" encoding="utf-8"?>""").append("\n")
        sb.append("<$rootTag\n")
            .append("    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n")
        if (rootTag.endsWith("ConstraintLayout")) {
            sb.append("    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n")
        }
        sb.append("    android:layout_width=\"match_parent\"\n")
            .append("    android:layout_height=\"match_parent\">\n")

        // 3) add each view
        activity.views.forEachIndexed { idx, view ->
            // find matching constraint info or default
            val ci = constraints.find { it.viewId == view.id }
            val w = ci?.attributes?.get("layout_width")  ?: "wrap_content"
            val h = ci?.attributes?.get("layout_height") ?: "wrap_content"

            sb.append("    <${view.type}\n")
                .append("        android:id=\"@+id/${view.id}\"\n")
                .append("        android:layout_width=\"$w\"\n")
                .append("        android:layout_height=\"$h\"")

            // append any extra app: constraints
            if (ci != null) {
                ci.attributes.forEach { (attr, value) ->
                    if (attr.startsWith("constraint")) {
                        // e.g. "constraintStart_toStartOf" -> app:layout_constraintStart_toStartOf
                        val xmlAttr = "app:layout_" + attr.removePrefix("constraint")
                        sb.append("\n        $xmlAttr=\"$value\"")
                    }
                }
            }

            sb.append(" />\n")
        }

        sb.append("</$rootTag>\n")
        return if (prettify) prettifyXml(sb.toString()) else sb.toString()
    }

    private fun prettifyXml(xml: String): String {
        // simple no-op for now; you could hook in an XML formatter library
        return xml
    }
}

