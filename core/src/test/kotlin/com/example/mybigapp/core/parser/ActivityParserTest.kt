package com.example.mybigapp.core.parser

import com.parser.ActivityParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ActivityParserTest {
    private val sampleSource = """
    class MyActivity : AppCompatActivity() {
      override fun onCreate(saved: Bundle?) {
        super.onCreate(saved)
        setContentView(R.layout.activity_main)
        val submitBtn: Button = findViewById(R.id.submitButton)
        submitBtn.setOnClickListener { /*...*/ }
      }
    }
  """.trimIndent()

    @Test
    fun `parser extracts layout, views and click handlers`() {
        val parsed = ActivityParser.parse(sampleSource)
        assertNotNull(parsed)
        assertEquals("activity_main", parsed.layoutFile)
        assertEquals(1, parsed.views.size)
        assertEquals("submitButton", parsed.views[0].id)
        assertEquals("Button", parsed.views[0].type)
        assertEquals(1, parsed.clickHandlers.size)
        assertEquals("onSubmitButtonClick", parsed.clickHandlers[0].methodName)    }
}
