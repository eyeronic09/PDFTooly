package com.eyeronic09.freepdf.homescreen.domain.Repository

import android.net.Uri
import com.eyeronic09.freepdf.homescreen.domain.model.PdfFile

interface PdfReposistory {
    suspend fun getAllPdfs(): List<PdfFile>


    suspend fun searchPdfs(byname: String): List<Uri>

    suspend fun deletePdf(uri: Uri): Boolean
}