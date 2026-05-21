package com.system.sticx

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

class StickerContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.system.sticx.stickercontentprovider"
        // Variabel statis untuk menyimpan URI file .webp yang lu pilih di galeri/file manager
        var uristikerTerpilih: Uri? = null
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, "metadata", 1)
        addURI(AUTHORITY, "stickers/pack1", 2)
        addURI(AUTHORITY, "stickers_asset/pack1/*", 3)
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            1 -> {
                val cursor = MatrixCursor(arrayOf(
                    "android_play_store_link", "ios_app_store_link",
                    "sticker_pack_id", "sticker_pack_name",
                    "sticker_pack_publisher", "sticker_pack_tray_icon",
                    "is_whitelisted", "image_data_version", "avoid_cache"
                ))
                // Menjadikan file .webp lu sekaligus sebagai Icon Tray (sampul pack) di WA
                cursor.addRow(arrayOf("", "", "pack1", "Sticker Rycl", "Sticx Engine", "stiker_anda.webp", 0, 1, 0))
                cursor
            }
            2 -> {
                val cursor = MatrixCursor(arrayOf("sticker_file_name", "sticker_emoji"))
                cursor.addRow(arrayOf("stiker_anda.webp", "😎,🔥"))
                cursor
            }
            else -> null
        }
    }

    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        // Ketika WA meminta file gambar stikernya, kita suapi langsung pakai URI file yang lu pilih tadi
        if (uriMatcher.match(uri) == 3) {
            val fileUri = uristikerTerpilih ?: return null
            return try {
                context?.contentResolver?.openAssetFileDescriptor(fileUri, "r")
            } catch (e: Exception) {
                null
            }
        }
        return null
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

