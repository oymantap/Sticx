package com.system.sticx

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class WebpFrame(val id: Int, val uriString: String? = null)

@Composable
fun StickerEditorScreen(navController: NavController) {
    var textOverlay by remember { mutableStateOf("") }
    var frames by remember { mutableStateOf(listOf(WebpFrame(1))) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Sticx Editor Studio", style = MaterialTheme.typography.headlineSmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Canvas Area (Simulasi)
        Card(modifier = Modifier.fillMaxWidth().height(250.dp)) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(textOverlay, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Teks
        OutlinedTextField(
            value = textOverlay,
            onValueChange = { textOverlay = it },
            label = { Text("Tambahkan Teks") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Per-frame Editor untuk Animated WebP
        Text("Frames (Animated WebP)", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(frames) { frame ->
                Card(modifier = Modifier.size(80.dp)) {
                    Box(contentAlignment = androidx.compose.ui.Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("F-${frame.id}")
                    }
                }
            }
            item {
                Button(onClick = { frames = frames + WebpFrame(frames.size + 1) }) {
                    Text("+ Frame")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Logic compile frames jadi WebP lalu push ke WA */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Export & Tambah ke WhatsApp")
        }
    }
}
