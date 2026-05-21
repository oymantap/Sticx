package com.system.sticx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun QuickUploadScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Quick Upload (Udah Jadi)", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { /* Trigger File Picker khusus nyari .webp atau gambar */ },
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Pilih File Sticker (.webp / Gambar)")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { 
                // Panggil WhatsApp Integration Intent di bawah
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kirim Langsung ke WhatsApp")
        }
    }
}
