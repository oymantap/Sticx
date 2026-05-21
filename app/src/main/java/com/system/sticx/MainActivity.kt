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
            // Launcher untuk membuka File Manager asli Android (Storage Access Framework)
            val filePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()
            ) { uri: Uri? ->
                if (uri != null) {
                    try {
                        // Minta izin akses persisten sistem agar file bisa dibaca oleh WhatsApp via provider
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        // Simpan lokasi file yang lu pilih ke Provider
                        StickerContentProvider.uristikerTerpilih = uri
                        
                        // Eksekusi kirim ke WhatsApp
                        kirimKeWhatsApp()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Gagal memproses file berkas", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Batal memilih file", Toast.LENGTH_SHORT).show()
                }
            }

            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Sticx Sticker Injector", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(onClick = { 
                            // Membuka File Manager murni untuk memilih berkas gambar/webp
                            filePickerLauncher.launch(arrayOf("image/*")) 
                        }) {
                            Text(text = "PILIH FILE .WEBP LU & KIRIM")
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
            putExtra("sticker_pack_authority", StickerContentProvider.AUTHORITY)
            putExtra("sticker_pack_name", "Sticker Rycl")
            type = "vnd.android.cursor.item/vnd.com.system.sticx.sticker"
        }
        
        try {
            // Coba kirim ke WhatsApp standar
            intent.setPackage("com.whatsapp")
            startActivity(intent)
        } catch (e: Exception) {
            try {
                // Coba kirim ke WhatsApp Business jika WA biasa tidak ada
                intent.setPackage("com.whatsapp.w4b")
                startActivity(intent)
            } catch (err: Exception) {
                Toast.makeText(this, "WhatsApp tidak merespon/tidak terpasang!", Toast.LENGTH_LONG).show()
            }
        }
    }
}

