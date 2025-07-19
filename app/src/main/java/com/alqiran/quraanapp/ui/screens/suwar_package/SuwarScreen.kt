package com.alqiran.quraanapp.ui.screens.suwar_package

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val suwarListNumber = suwarListAndServer.surahList.split(",")
        items(suwarListNumber.size) { index ->

            Column (
                modifier = Modifier
                    .surfaceModifier()
                    .clickable {
                        mediaPlayer?.release()
                        mediaPlayer =
                            playSurah(suwarListNumber, index, suwarListAndServer.server)
                    }
                    .padding(16.dp)

            ) {
                Text(
                    text = "${suwarListNumber[index]} - سورة ${allSuwar.suwar[suwarListNumber[index].toInt() - 1].name}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                )

            }
        }
    }
}

fun playSurah(suwarList: List<String>, index: Int, server: String): MediaPlayer {
    val surahUrl = server + suwarList[index].padStart(3, '0')+ ".mp3"
    Log.d("Al-qiran", surahUrl)
    return MediaPlayer().apply {
        setDataSource(surahUrl)
        setOnPreparedListener { it.start() }
        setOnCompletionListener {
            if (suwarList.size - 1 == index)  {
                Log.d("Al-qiran", "$surahUrl $index ${suwarList.size} ${suwarList[index]}")
                playSurah(suwarList, 0, server)
            }
            else  {
                Log.e("Al-qiran", "$surahUrl $index ${suwarList.size} ${suwarList[index]}")
                playSurah(suwarList, index + 1, server)
            }
        }
        setOnErrorListener { _,  _, _ ->
            Log.d("Al-qiran", "entered set on Error $surahUrl $index ${suwarList.size} ${suwarList[index]}")
            true
        }
        prepareAsync()
    }
}