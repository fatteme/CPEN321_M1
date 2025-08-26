package com.cpen321.usermanagement.util

import android.content.Context
import android.net.Uri
import java.io.File

object MediaUtils {
    fun uriToFile(context: Context, uri: Uri): File {
        return when (uri.scheme) {
            "file" -> File(uri.path!!)
            "content" -> {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File.createTempFile("profile_", ".jpg", context.cacheDir)
                inputStream?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                file
            }
            else -> throw IllegalArgumentException("Unsupported URI scheme: ${uri.scheme}")
        }
    }
}
