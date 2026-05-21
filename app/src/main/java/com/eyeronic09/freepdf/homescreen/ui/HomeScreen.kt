package com.eyeronic09.freepdf.homescreen.ui

import android.os.Environment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.eyeronic09.freepdf.R
import com.eyeronic09.freepdf.homescreen.Utility.PermissionHandler
import org.koin.androidx.compose.koinViewModel

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(0u , title = "Notes", icon = painterResource(R.drawable.outline_devices_24))

    @Composable
    override fun Content() {
        Navigator(_HomeScreen())
    }

}
class _HomeScreen : Screen {

    @Composable
    override fun Content() {
        HomeScreenRoute()
    }

    @Composable
    fun HomeScreenRoute(viewModel: HomeScreenViewModel = koinViewModel()){

        val state by viewModel.pdfFiles.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        HomeScreen(state = state , event)
    }

    @Composable
    fun HomeScreen(state: HomeScreenUiState, onEvent: (HomeScreenOnEvent) -> Unit) {
        Box(modifier = Modifier.fillMaxSize()){
            Scaffold (){ innerPadding ->
                HomeScreenContent(state , modifier = Modifier.padding(innerPadding) ,onEvent)
            }
        }

    }

    @Composable
    fun HomeScreenContent(state: HomeScreenUiState, modifier: Modifier , onEvent: (HomeScreenOnEvent) -> Unit  ){
        val pdfList = state.pdfFiles
        PermissionHandler(
            onPermissionGranted = {
                onEvent(HomeScreenOnEvent.loadPdf)
            }
        )
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(pdfList) { pdf ->
                ListItem(
                    headlineContent = { Text(pdf.name) },
                    leadingContent = {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                    }
                )
            }
        }

    }
}

