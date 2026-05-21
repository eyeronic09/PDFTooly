package com.eyeronic09.freepdf.homescreen.data.reposistory

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.eyeronic09.freepdf.homescreen.domain.Repository.PdfReposistory
import com.eyeronic09.freepdf.homescreen.domain.model.PdfFile

class ReposistoryImpl(private val context: Context) : PdfReposistory   {
    override suspend fun getAllPdfs(): List<PdfFile> {
        val pdfFiles = mutableListOf<PdfFile>()
        val internalStroagePath = Environment.getExternalStorageDirectory().absolutePath
        val contection = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE
        )

        val selection = """
            ${MediaStore.Files.FileColumns.MIME_TYPE} = ?
            AND ${MediaStore.Files.FileColumns.DATA} LIKE ?
        """.trimIndent()
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"
        val selectionArgs = arrayOf(
            "application/pdf",
            "$internalStroagePath%"
        )
        val cursor = context.contentResolver.query( contection, projection, selection, selectionArgs, sortOrder)
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val path = it.getString(dataColumn)
                val size = it.getLong(sizeColumn)
                val contentUri = ContentUris.withAppendedId(contection, id)

                pdfFiles.add(PdfFile(
                    name = name,
                    path = path,
                    uri = contentUri,
                    size = size
                ))
                Log.d("pdfFiles", "Found PDF: ${pdfFiles}, Path: $path")
            }
        }
        return pdfFiles
    }

    override suspend fun searchPdfs(byname: String): List<Uri> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePdf(uri: Uri): Boolean {
        TODO("Not yet implemented")
    }

}