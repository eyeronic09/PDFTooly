package com.eyeronic09.freepdf.PDFviewer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.infomaniak.lib.pdfview.PDFView
import org.koin.androidx.compose.koinViewModel

class SecondActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uri = intent.dataString ?: intent.getStringExtra("uri")
            if (uri != null) {
                PdfViewer(uri = uri.toUri(), modifier = Modifier.fillMaxSize() , onBackButton = {finish()} )
            }
        }
    }

}
class _Pdfviewer(private val uri: String) : Screen {

    @Composable
    override fun Content() {
        val pdfUri = remember(uri) { uri.toUri() }
        val navigator = LocalNavigator.currentOrThrow
        PdfViewer(
            uri = pdfUri, modifier = Modifier.fillMaxSize(),
            onBackButton = { navigator.pop() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewer(uri: Uri?, modifier: Modifier = Modifier, onBackButton: () -> Unit , viewModel: PDFviewerViewModel = koinViewModel()) {
    LaunchedEffect(uri) {
        viewModel.onEvent(PDFViewerEvent.LoadUri(uri))
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val event = viewModel::onEvent
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = uri?.path.toString())},
                navigationIcon = {
                    IconButton(onClick = onBackButton) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back ")
                    }
                }
            )

        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        event.invoke(PDFViewerEvent.addTextToExistingFile(text = "hii", context = context , uri = uiState.value.openedFileUri))
                    } ){
                        Icon(Icons.Filled.Share, contentDescription = "Share PDF")
                    }
                    IconButton(onClick = { /* TODO: Implement info */ }) {
                        Icon(Icons.Filled.Info, contentDescription = "PDF Details")
                    }
                },
                floatingActionButton = {
                    // Optional: Add a FAB here if needed
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
fun InfomaniakPdfViewer(uri: Uri?, modifier: Modifier = Modifier) {
    if (uri == null) return
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
