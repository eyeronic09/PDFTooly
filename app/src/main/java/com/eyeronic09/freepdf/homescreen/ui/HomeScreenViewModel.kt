package com.eyeronic09.freepdf.homescreen.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeronic09.freepdf.homescreen.domain.Repository.PdfReposistory
import com.eyeronic09.freepdf.homescreen.domain.model.PdfFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeScreenUiState(
    val pdfFiles: List<PdfFile> = emptyList(),
    val error : String = "",
    val loading: Boolean = true
)
sealed interface HomeScreenOnEvent {
    object loadPdf : HomeScreenOnEvent
}
class HomeScreenViewModel(private val repository: PdfReposistory) : ViewModel() {
    private val _pdfFiles = MutableStateFlow(HomeScreenUiState())
    val pdfFiles: StateFlow<HomeScreenUiState> = _pdfFiles.asStateFlow()

    init {
        loadedPDF()
    }

    fun onEvent(event: HomeScreenOnEvent) {
        when (event) {
            HomeScreenOnEvent.loadPdf -> loadedPDF()
        }
    }

     fun loadedPDF(){
         viewModelScope.launch {
             try {
                 val files = repository.getAllPdfs()
                 _pdfFiles.update { currentState ->
                     currentState.copy(
                         pdfFiles = files,
                         loading = false
                     )
                 }
                 Log.d("files", files.toString())
             } catch (e: Exception) {
                 _pdfFiles.update { currentState ->
                     currentState.copy(
                         error = e.message ?: "Unknown Error",
                         loading = false
                     )
                 }
             }
         }
    }
}
