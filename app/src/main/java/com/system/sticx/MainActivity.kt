package com.system.sticx

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var inputNama by remember { mutableStateOf("") }
            var statusTray by remember { mutableStateOf("Belum pilih") }
            var statusS1 by remember { mutableStateOf("Belum pilih") }
            var statusS2 by remember { mutableStateOf("Belum pilih") }
            var statusS3 by remember { mutableStateOf("Belum pilih") }
            var isLoading by remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()

            fun sikatUri(uri: Uri?): Uri? {
                if (uri != null) {
                    try {
                        contentResolver.takePersistableUriPermission(
                            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (e: Exception) { e.printStackTrace() }
                }
                return uri
            }

            // Kunci filter picker hanya menerima file image/webp asli
            val webpFilter = arrayOf("image/webp")
            val pickTray = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { 
                if (it != null) { statusTray = "✅ Tray Ok"; StickerContentProvider.uriTray = sikatUri(it) }
            }
            val pickS1 = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { 
                if (it != null) { statusS1 = "✅ Stiker 1 Ok"; StickerContentProvider.uriStiker1 = sikatUri(it) }
            }
            val pickS2 = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { 
                if (it != null) { statusS2 = "✅ Stiker 2 Ok"; StickerContentProvider.uriStiker2 = sikatUri(it) }
            }
            val pickS3 = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { 
                if (it != null) { statusS3 = "✅ Stiker 3 Ok"; StickerContentProvider.uriStiker3 = sikatUri(it) }
            }

            MaterialTheme {
                Scaffold { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Sticx Sticker Creator", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputNama,
                            onValueChange = { inputNama = it },
                            label = { Text("Nama Sticker Lu") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Publisher otomatis: Sticx", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(onClick = { pickTray.launch(webpFilter) }) { Text("Upload Sampul / Icon Tray (.webp)") }
                        Text(statusTray, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(onClick = { pickS1.launch(webpFilter) }) { Text("Upload Stiker 1 (.webp)") }
                        Text(statusS1, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(onClick = { pickS2.launch(webpFilter) }) { Text("Upload Stiker 2 (.webp)") }
                        Text(statusS2, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(onClick = { pickS3.launch(webpFilter) }) { Text("Upload Stiker 3 (.webp)") }
                        Text(statusS3, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(32.dp))

                        if (isLoading) {
                            CircularProgressIndicator()
                            Text("Mengompres & Menyiapkan data...", modifier = Modifier.padding(top = 8.dp))
                        } else {
                            Button(
                                onClick = {
                                    if (inputNama.trim().isEmpty() || 
                                        StickerContentProvider.uriTray == null || 
                                        StickerContentProvider.uriStiker1 == null || 
                                        StickerContentProvider.uriStiker2 == null || 
                                        StickerContentProvider.uriStiker3 == null) {
                                        Toast.makeText(this@MainActivity, "Isi nama & upload semua 4 file WebP asli!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        coroutineScope.launch {
                                            isLoading = true
                                            StickerContentProvider.namaPackInput = inputNama
                                            delay(1000)
                                            isLoading = false
                                            kirimKeWhatsApp()
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text("KIRIM PACK KE WHATSAPP")
                            }
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
            putExtra("sticker_pack_name", StickerContentProvider.namaPackInput)
            // Sesuai koreksi lu: TYPE DIHAPUS biar WA kaga bingung
            setPackage("com.whatsapp")
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            try {
                intent.setPackage("com.whatsapp.w4b")
                startActivity(intent)
            } catch (err: Exception) {
                Toast.makeText(this, "WhatsApp menolak memproses paket stiker!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
