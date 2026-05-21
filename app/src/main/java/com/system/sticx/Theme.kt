package com.system.sticx

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ModernDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),     // Neon Violet
    secondary = Color(0xFF38E54D),   // WhatsApp Green Light
    background = Color(0xFF0B0B0E),  // Deep Matte Black
    surface = Color(0xFF16161F),     // Card Surface
    onPrimary = Color(0xFF381E72)
)

@Composable
fun SticxTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ModernDarkColorScheme,
        content = content
    )
}
