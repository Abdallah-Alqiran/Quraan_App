package com.alqiran.quraanapp.ui.screens.suwar_package

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar
import com.alqiran.quraanapp.ui.components.loading_and_failed.FailedLoadingScreen
import com.alqiran.quraanapp.ui.components.loading_and_failed.LoadingProgressIndicator
import com.alqiran.quraanapp.ui.components.modifiers.surfaceModifier
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModel.SuwarState
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModel.SuwarViewModel

@Composable
fun SuwarScreen(suwarListAndServer: RecitersMoshafReading) {

    val suwarViewModel = hiltViewModel<SuwarViewModel>()
    val state by suwarViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        suwarViewModel.fetchSuwar()
    }

    when (state) {
        is SuwarState.Error -> {
            FailedLoadingScreen((state as SuwarState.Error).message) {
                suwarViewModel.fetchSuwar()
            }
        }

        SuwarState.Loading -> {
            LoadingProgressIndicator()
        }

        is SuwarState.Success -> {
            PrintAllSuwar(
                suwarListAndServer = suwarListAndServer,
                allSuwar = (state as SuwarState.Success).allSuwar
            )
        }
    }
}

@Composable
fun PrintAllSuwar(suwarListAndServer: RecitersMoshafReading, allSuwar: AllSuwar) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(suwarListAndServer.surahList.split(",")) { surahNumber ->
            Row(
                modifier = Modifier
                    .surfaceModifier()
                    .padding(16.dp)

            ) {
                Text(
                    text = "$surahNumber- ${allSuwar.suwar[surahNumber.toInt()-1].name} سورة ",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier

                )

            }
        }
    }
}