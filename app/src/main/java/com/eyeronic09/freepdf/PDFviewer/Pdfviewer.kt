package com.eyeronic09.freepdf.PDFviewer

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.infomaniak.lib.pdfview.PDFView

class _Pdfviewer(private val uri: String) : Screen {
    @Composable
    override fun Content() {
        val pdfUri = remember(uri) { uri.toUri() }
        PdfViewer(uri = pdfUri, modifier = Modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewer(uri: Uri, modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.currentOrThrow
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = uri.path.toString())},
                navigationIcon = {
                    IconButton(onClick = { navigator.popUntilRoot() }) {
                        Text(text = "text")
                    }
                }
            )
        }
    ) { it  ->
        InfomaniakPdfViewer(
            uri = uri,
            modifier = modifier.padding(it)

        )}
}


@Composable
fun InfomaniakPdfViewer(uri: Uri, modifier: Modifier = Modifier) {


    AndroidView(
        factory = { context ->
            PDFView(context, null)
        },
        update = { pdfView ->
            pdfView.fromUri(uri)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableAnnotationRendering(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .nightMode(false)
                .load()

        },
        modifier = modifier
    )
}
