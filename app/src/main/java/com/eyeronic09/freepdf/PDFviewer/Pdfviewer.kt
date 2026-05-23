package com.eyeronic09.freepdf.PDFviewer

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import com.rajat.pdfviewer.util.PdfSource

class _Pdfviewer(private val uri: String) : Screen {
    @Composable
    override fun Content() {
        val uri = remember(uri) { uri.toUri() }
        PdfViewerScreen(uri)
    }

}
@Composable
fun PdfViewerScreen(
    uri: Uri,
    modifier: Modifier = Modifier
) {
    PdfRendererViewCompose(
            source = PdfSource.LocalUri(uri),
            modifier = Modifier.fillMaxSize(),
            lifecycleOwner = LocalLifecycleOwner.current,

    )
}