package com.eyeronic09.freepdf.PDFviewer

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PDFViewerState(
    val openedFileUri: Uri?= null,
    val isLoading: Boolean = false
)
sealed interface PDFViewerEvent {
    data class LoadUri(val uri: Uri?) : PDFViewerEvent
    data class addTextToExistingFile
        (val text: String? = "", val context: Context, val uri: Uri?) : PDFViewerEvent
}
class PDFviewerViewModel () : ViewModel() {
    private val _uiState = MutableStateFlow(PDFViewerState())
    val uiState : StateFlow<PDFViewerState> = _uiState.asStateFlow()

    fun onEvent(event: PDFViewerEvent) {
        when (event) {
            is PDFViewerEvent.LoadUri -> {
                _uiState.update { it.copy(openedFileUri = event.uri) }
            }

            is PDFViewerEvent.addTextToExistingFile -> {
                addTextToExistingFile(
                    context = event.context,
                    text = event.text,
                    uri = event.uri
                )
            }
        }
    }

    fun addTextToExistingFile(
        context: Context,
        uri: Uri?,
        text: String? = ""
    ) {
        if (uri == null) return

        viewModelScope.launch(Dispatchers.IO) {

            _uiState.update { it.copy(isLoading = true) }

            try {
                PDFBoxResourceLoader.init(context)

                context.contentResolver.openInputStream(uri)?.use { inputStream ->

                    val document = PDDocument.load(inputStream)

                    val page = document.getPage(0)

                    PDPageContentStream(
                        document,
                        page,
                        PDPageContentStream.AppendMode.APPEND,
                        true,
                        true
                    ).use { contentStream ->

                        contentStream.beginText()

                        contentStream.setFont(
                            PDType1Font.TIMES_BOLD,
                            525f
                        )

                        contentStream.newLineAtOffset(
                            325f,
                            32f
                        )

                        contentStream.showText(text ?: "")

                        contentStream.endText()
                    }

                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        document.save(outputStream)
                    }

                    document.close()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}