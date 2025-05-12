package com.parser

/**
 * Parses a Kotlin Activity/Fragment source string to extract:
 *  • one or more layout file names via setContentView
 *  • all view references: generic, declaration, cast, ViewBinding, synthetic
 *  • any setOnClickListener handlers
 */
object ActivityParser {
    private val layoutRegex = Regex("""setContentView\(\s*R\.layout\.(\w+)\s*\)""")
    private val genericRegex = Regex("""findViewById<(?<type>\w+)>\(\s*R\.id\.(?<id>\w+)\s*\)""")
    private val declRegex    = Regex("""val\s+(?<var>\w+)\s*:\s*(?<type>\w+)\s*=\s*findViewById\(\s*R\.id\.(?<id>\w+)\s*\)""")
    private val castRegex    = Regex("""val\s+(?<var>\w+)\s*=\s*findViewById\(\s*R\.id\.(?<id>\w+)\s*\)\s+as\s+(?<type>\w+)""")
    private val bindingRegex = Regex("""binding\.(?<id>\w+)""")
    private val clickRegex   = Regex("""(?<var>\w+)\.setOnClickListener""")

    fun parse(source: String): ParsedActivity? {
        val layouts = layoutRegex.findAll(source).map { it.groupValues[1] }.toList()
        if (layouts.isEmpty()) return null

        val varToId = mutableMapOf<String, String>()
        val views = mutableListOf<ViewInfo>()
        val clicks = mutableListOf<ClickHandlerInfo>()

        // Generic findViewById<T>
        genericRegex.findAll(source).forEach {
            views += ViewInfo(id = it.groupValues[2], type = it.groupValues[1])
        }
        // Declaration style
        declRegex.findAll(source).forEach {
            val varName = it.groups[1]!!.value
            val type    = it.groups[2]!!.value
            val id      = it.groups[3]!!.value
            varToId[varName] = id
            views += ViewInfo(id = id, type = type)
        }
        // Cast style
        castRegex.findAll(source).forEach {
            val varName = it.groups[1]!!.value
            val id      = it.groups[2]!!.value
            val type    = it.groups[3]!!.value
            varToId[varName] = id
            views += ViewInfo(id = id, type = type)
        }
        // ViewBinding
        bindingRegex.findAll(source).forEach {
            val id = it.groupValues[1]
            varToId[id] = id
            if (views.none { v -> v.id == id }) {
                views += ViewInfo(id = id, type = "View")
            }
        }

        // Click handlers
        clickRegex.findAll(source).forEach {
            val varName = it.groupValues[1]
            val id = varToId[varName] ?: varName
            if (views.none { v -> v.id == id }) {
                views += ViewInfo(id = id, type = "View")
            }
            clicks += ClickHandlerInfo(
                methodName = "on${id.replaceFirstChar(Char::uppercase)}Click",
                viewId     = id
            )
        }

        // Dedupe
        val deduped = views.distinctBy { it.id }

        return ParsedActivity(
            classNames    = layouts,
            views         = deduped,
            clickHandlers = clicks,
            layoutFile    = layouts.first()
        )
    }
}

data class ParsedActivity(
    val classNames: List<String>,
    val views: List<ViewInfo>,
    val clickHandlers: List<ClickHandlerInfo>,
    val layoutFile: String
)

data class ViewInfo(val id: String, val type: String)

data class ClickHandlerInfo(val methodName: String, val viewId: String)