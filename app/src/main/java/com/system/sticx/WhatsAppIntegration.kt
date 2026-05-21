package com.system.sticx

import android.content.Context
import android.content.Intent
import android.net.Uri

object WhatsAppIntegration {
    // WhatsApp Authority resmi
    private const val WA_INTENT_ACTION = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
    const val EXTRA_STICKER_PACK_ID = "sticker_pack_id"
    const val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
    const val EXTRA_STICKER_PACK_NAME = "sticker_pack_name"

    fun addStickerPackToWhatsApp(context: Context, packId: String, packName: String) {
        val intent = Intent().apply {
            action = WA_INTENT_ACTION
            putExtra(EXTRA_STICKER_PACK_ID, packId)
            putExtra(EXTRA_STICKER_PACK_AUTHORITY, "com.system.sticx.stickercontentprovider")
            putExtra(EXTRA_STICKER_PACK_NAME, packName)
        }
        
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Handle kalau WA tidak terinstall
            e.printStackTrace()
        }
    }
}
