// app/src/main/java/com/example/mybigapp/ui/theme/Theme.kt
package com.example.mybigapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Your colors from Color.kt
// (No "color =" named args)
private val LightColorScheme = lightColorScheme(
    primary          = Purple500,
    primaryContainer = Purple200,
    secondary        = Teal200,

    background       = Color.White,
    surface          = Color.White,

    onPrimary        = Color.White,
    onSecondary      = Color.Black,
    onBackground     = Color.Black,
    onSurface        = Color.Black
)

@Composable
fun MyBigAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = Typography(),  // Material-3 Typography
        shapes      = Shapes(),      // Material-3 Shapes
        content     = content
    )
}
