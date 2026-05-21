package com.system.sticx

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Sticx Sticker Injector Engine", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(onClick = { kirimKeWhatsApp() }) {
                            Text(text = "KIRIM STIKER KE WHATSAPP")
                        }
                    }
                }
            }
        }
    }

    // Fungsi "Alat" buat nembak data stiker langsung tembus ke WA
    private fun kirimKeWhatsApp() {
        val intent = Intent().apply {
            action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
            putExtra("sticker_pack_id", "pack1")
            putExtra("sticker_pack_authority", "com.system.sticx.stickercontentprovider")
            putExtra("sticker_pack_name", "Sticker Rycl")
            type = "vnd.android.cursor.item/vnd.com.system.sticx.sticker"
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Handle kalau WA ga terinstall atau error
            e.printStackTrace()
        }
    }
}

