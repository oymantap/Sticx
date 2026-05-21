package com.system.sticx

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import java.io.File

class StickerContentProvider : ContentProvider() {

    private val authority = "com.system.sticx.stickercontentprovider"
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(authority, "metadata", 1)
        addURI(authority, "stickers/*", 2)
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val code = uriMatcher.match(uri)
        val cursor = MatrixCursor(arrayOf("sticker_pack_id", "sticker_pack_name", "sticker_pack_publisher", "sticker_file_name"))
        
        if (code == 1) {
            // Kasih tahu WhatsApp info pack stiker lu
            cursor.addRow(arrayOf("pack1", "Sticker Rycl", "Sticx Engine", "logo.webp"))
        } else if (code == 2) {
            // WhatsApp minta daftar file stiker di dalam pack
            // Di sini kita daftarin file .webp lu (misal nama filenya: stiker1.webp)
            cursor.addRow(arrayOf("pack1", "Sticker Rycl", "Sticx Engine", "stiker1.webp"))
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            1 -> "vnd.android.cursor.dir/vnd.com.system.sticx.sticker"
            2 -> "vnd.android.cursor.item/vnd.com.system.sticx.sticker"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}

