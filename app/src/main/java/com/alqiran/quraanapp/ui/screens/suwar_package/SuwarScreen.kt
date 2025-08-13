package com.alqiran.quraanapp.ui.screens.suwar_package

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startForegroundService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar
import com.alqiran.quraanapp.ui.components.loading_and_failed.FailedLoadingScreen
import com.alqiran.quraanapp.ui.components.loading_and_failed.LoadingProgressIndicator
import com.alqiran.quraanapp.data.datasources.remote.model.Audio
import com.alqiran.quraanapp.player.service.AudioService
import com.alqiran.quraanapp.ui.components.modifiers.surfaceModifier
import com.alqiran.quraanapp.ui.screens.suwar_package.components.BottomBarPlayer
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel.AudioEvents
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel.AudioViewModel
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel.SuwarState
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel.SuwarViewModel


@Composable
fun SuwarScreen(suwarListAndServer: RecitersMoshafReading, reciterName: String) {

    val suwarViewModel = hiltViewModel<SuwarViewModel>()
    val audioViewModel = hiltViewModel<AudioViewModel>()
    val state by suwarViewModel.state.collectAsStateWithLifecycle()

    var isServiceRunning = false

    val context = LocalContext.current

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

            val suwarListNumber = suwarListAndServer.surahList.split(",")

            val audioList = remember(suwarListAndServer.surahList, suwarListAndServer.server, reciterName) {
                suwarListNumber.map { surahNumber ->
                    Audio(
                        surahNumber = surahNumber,
                        server = suwarListAndServer.server,
                        surah = (state as SuwarState.Success).allSuwar.suwar[surahNumber.toInt() - 1].name,
                        reciter = reciterName,
                        duration = 0
                    )
                }
            }

            LaunchedEffect(audioList) {
                audioViewModel.setAllAudioData(audioList)
            }

            PrintAllSuwar(
                suwarListAndServer = suwarListAndServer,
                allSuwar = (state as SuwarState.Success).allSuwar,
                reciterName = reciterName,
                progress = audioViewModel.progress,
                progressTimer = audioViewModel.progressTimer,
                duration = audioViewModel.duration,
                onProgress = { audioViewModel.onAudioEvents(AudioEvents.SeekTo(it)) },
                isAudioPlaying = audioViewModel.isPlaying,
                audioList = audioList,
                currentPlayingAudio = audioViewModel.currentSelectedAudio,
                onStart = {
                    audioViewModel.onAudioEvents(AudioEvents.PlayPause)
                },
                onItemClick = {
                    val surahIndex = it
                    val surahNumber = suwarListNumber[surahIndex].toInt()
                    audioViewModel.onAudioEvents(AudioEvents.SelectedAudioChange(surahIndex))
                    if (!isServiceRunning) {
                        val intent = Intent(context, AudioService::class.java)
                        startForegroundService(context, intent)
                    }
                    isServiceRunning = true
                },
                onNext = {
                    audioViewModel.onAudioEvents(AudioEvents.SeekToNext)
                },
                onPrevious = {
                    audioViewModel.onAudioEvents(AudioEvents.SeekToPrevious)
                }
            )
        }
    }
}

@Composable
fun PrintAllSuwar(
    suwarListAndServer: RecitersMoshafReading,
    allSuwar: AllSuwar,
    reciterName: String,
    progress: Float = 0f,
    progressTimer: Float = 0f,
    duration: Long = 0L,
    onProgress: (Float) -> Unit,
    isAudioPlaying: Boolean = false,
    audioList: List<Audio>,
    currentPlayingAudio: Audio = Audio(),
    audiList: List<Audio> = listOf(),
    onStart: () -> Unit,
    onItemClick: (Int) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
) {

    Scaffold(
        bottomBar = {
            BottomBarPlayer(
                progress = progress,
                progressTimer = progressTimer,
                onProgress = onProgress,
                duration = duration,
                audio = currentPlayingAudio,
                isAudioPlaying = isAudioPlaying,
                onStart = {
                    onStart()
                },
                onNext = {
                    onNext()
                },
                onPrevious = {
                    onPrevious()
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = it
        ) {
            val suwarListNumber = suwarListAndServer.surahList.split(",")
            items(suwarListNumber.size) { index ->
                Column(
                    modifier = Modifier
                        .surfaceModifier(shape = RoundedCornerShape(8.dp))
                        .clickable {
                            onItemClick(index)
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
}
