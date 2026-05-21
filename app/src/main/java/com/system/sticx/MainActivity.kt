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
            // Launcher buat buka File Manager asli HP
            val filePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()
            ) { uri: Uri? ->
                if (uri != null) {
                    // Ambil hak akses file, lalu langsung panggil WhatsApp
                    try {
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    kirimKeWhatsApp()
                } else {
                    Toast.makeText(this, "Batal pilih file", Toast.LENGTH_SHORT).show()
                }
            }

            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Sticx Sticker Injector", 
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(onClick = { 
                            // Buka file manager dan filter file gambar/webp
                            filePickerLauncher.launch(arrayOf("image/*")) 
                        }) {
                            Text(text = "PILIH .WEBP & TAMBAH KE WA")
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
            // Kalau gagal pakai intent umum, paksa tembak paket WA resmi
            try {
                intent.setPackage("com.whatsapp")
                startActivity(intent)
            } catch (err: Exception) {
                Toast.makeText(this, "Gagal membuka WhatsApp!", Toast.LENGTH_LONG).show()
            }
        }
    }
}

