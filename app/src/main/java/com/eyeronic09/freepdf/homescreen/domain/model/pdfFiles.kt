package com.eyeronic09.freepdf.homescreen.domain.model

import android.net.Uri

data class PdfFile(
    val name: String,
    val path: String,
    val uri: Uri,
    val size: Long,
)