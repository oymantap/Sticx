package com.system.sticx

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Bundle
import android.util.Log

class StickerContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.system.sticx.stickercontentprovider"
        var namaPackInput: String = "Sticx Pack"
        var uriTray: Uri? = null
        var uriStiker1: Uri? = null
        var uriStiker2: Uri? = null
        var uriStiker3: Uri? = null
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, "metadata", 1)
        addURI(AUTHORITY, "stickers/*", 2)
        addURI(AUTHORITY, "stickers_asset/*/*", 3)
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        Log.d("STICX_URI_QUERY", uri.toString())
        
        return when (uriMatcher.match(uri)) {
            1 -> {
                // Schema urutan kolom metadata resmi WA
                val cursor = MatrixCursor(arrayOf(
                    "sticker_pack_id", "sticker_pack_name", "sticker_pack_publisher",
                    "sticker_pack_tray_icon", "android_play_store_link", "ios_app_store_link",
                    "is_whitelisted", "image_data_version", "avoid_cache"
                ))
                // is_whitelisted gua set 1 (true) biar ga kena silent reject ampas
                cursor.addRow(arrayOf("pack1", namaPackInput, "Sticx", "tray.webp", "", "", 1, 1, 0))
                cursor
            }
            2 -> {
                // Schema urutan kolom list stiker resmi WA
                val cursor = MatrixCursor(arrayOf("sticker_file_name", "sticker_emoji"))
                cursor.addRow(arrayOf("s1.webp", "😎"))
                cursor.addRow(arrayOf("s2.webp", "🔥"))
                cursor.addRow(arrayOf("s3.webp", "🚀"))
                cursor
            }
            else -> null
        }
    }

    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        Log.d("STICX_URI_ASSET", uri.toString())
        
        if (uriMatcher.match(uri) == 3) {
            val fileName = uri.pathSegments.last()
            val targetUri = when (fileName) {
                "tray.webp" -> uriTray
                "s1.webp" -> uriStiker1
                "s2.webp" -> uriStiker2
                "s3.webp" -> uriStiker3
                else -> null
            } ?: return null

            return try {
                context?.contentResolver?.openAssetFileDescriptor(targetUri, "r")
            } catch (e: Exception) {
                Log.e("STICX_ERROR_FILE", "Gagal open file descriptor: ${e.message}")
                null
            }
        }
        return null
    }

    // SESAJEN UTAMA: override method call() untuk bypass deteksi whitelist WA modern
    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        if (method == "is_whitelisted") {
            val bundle = Bundle()
            bundle.putBoolean("is_whitelisted", true)
            return bundle
        }
        return super.call(method, arg, extras)
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            1 -> "vnd.android.cursor.dir/vnd.com.system.sticx.sticker"
            2 -> "vnd.android.cursor.item/vnd.com.system.sticx.sticker"
            3 -> "image/webp"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}
