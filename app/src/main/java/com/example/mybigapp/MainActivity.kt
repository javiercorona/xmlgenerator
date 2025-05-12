package com.example.mybigapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.generator.XmlGenerator
import com.parser.ActivityParser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutGeneratorScreen()
        }
    }
}

@Composable
fun LayoutGeneratorScreen() {
    // State holders for input Kotlin code and generated XML
    var kotlinCode by remember { mutableStateOf(
        "// Paste your Activity code here\nclass MyActivity : AppCompatActivity() { … }"
    ) }
    var xmlOutput by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Text("Kotlin Source:")
        Spacer(Modifier.height(8.dp))
        BasicTextField(
            value = kotlinCode,
            onValueChange = { kotlinCode = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            // Call into core stubs (they currently return null or skeleton XML)
            val parsed = ActivityParser.parse(kotlinCode)
            xmlOutput = if (parsed != null) {
                XmlGenerator().generateLayoutXml(parsed, prettify = true)
            } else {
                "<!-- parse failed -->"
            }
        }) {
            Text("Generate XML")
        }
        Spacer(Modifier.height(16.dp))

        Text("Generated XML:")
        Spacer(Modifier.height(8.dp))
        BasicTextField(
            value = xmlOutput,
            onValueChange = { /* read-only */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}
