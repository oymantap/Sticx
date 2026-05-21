package com.system.sticx

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
            // ALAT BUAT BUKA FILE MANAGER / GALERI
            val filePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                if (uri != null) {
                    // Kalau user selesai milih file .webp, langsung oper ke WhatsApp
                    kirimKeWhatsApp()
                    Toast.makeText(this, "File terpilih! Mengirim ke WhatsApp...", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Batal milih file", Toast.LENGTH_SHORT).show()
                }
            }

            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Sticx Sticker Injector Engine", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // TOMBOL UTAMA BUAT BUKA GALERI
                        Button(onClick = { 
                            // Membuka file picker khusus untuk mencari file .webp atau gambar
                            filePickerLauncher.launch("image/*") 
                        }) {
                            Text(text = "PILIH .WEBP & KIRIM KE WA")
                        }
                    }
                }
            }
        }
    }

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
            Toast.makeText(this, "WhatsApp tidak ditemukan!", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}

