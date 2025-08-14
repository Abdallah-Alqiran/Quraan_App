package com.alqiran.quraanapp.ui.screens.suwar_package

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.alqiran.quraanapp.ui.components.search.DefaultSearchField
import com.alqiran.quraanapp.ui.screens.suwar_package.components.BottomBarPlayer
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel.AudioEvents
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel.AudioViewModel
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel.SuwarState
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel.SuwarViewModel
import com.alqiran.quraanapp.ui.utils.surahNameToIdMap


@Composable
fun SuwarScreen(suwarListAndServer: RecitersMoshafReading, reciterName: String) {

    val suwarViewModel = hiltViewModel<SuwarViewModel>()
    val audioViewModel = hiltViewModel<AudioViewModel>()
    val state by suwarViewModel.state.collectAsStateWithLifecycle()
    val searchText by suwarViewModel.searchText.collectAsState()

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
            val suwar = (state as SuwarState.Success).allSuwar.suwar

            LaunchedEffect(Unit) {
                val audioList =
                    suwar.map { surah ->
                        Audio(
                            surahNumber = surahNameToIdMap[surah.name].toString(),
                            server = suwarListAndServer.server,
                            surah = surah.name,
                            reciter = reciterName,
                        )
                    }
                Log.d("Al-qiran", "$audioList")
                audioViewModel.setAllAudioData(audioList)
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                DefaultSearchField(
                    text = searchText,
                    placeHolder = "ابحث عن السورة",
                    onTextChange = {
                        suwarViewModel.onSearchTextChange(it)
                    })
                PrintAllSuwar(
                    allSuwar = (state as SuwarState.Success).allSuwar,
                    progress = audioViewModel.progress,
                    progressTimer = audioViewModel.progressTimer,
                    duration = audioViewModel.duration,
                    onProgress = { audioViewModel.onAudioEvents(AudioEvents.SeekTo(it)) },
                    isAudioPlaying = audioViewModel.isPlaying,
                    currentPlayingAudio = audioViewModel.currentSelectedAudio,
                    onStart = {
                        audioViewModel.onAudioEvents(AudioEvents.PlayPause)
                    },
                    onItemClick = {
                        val surahIndex = it
                        audioViewModel.onAudioEvents(AudioEvents.SelectedAudioChange(surahIndex))
                        if (!isServiceRunning) {
                            val intent = Intent(context, AudioService::class.java)
                            startForegroundService(context, intent)
                        }
                        isServiceRunning = true
                        suwarViewModel.onSearchTextChange("")
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
}

@Composable
fun PrintAllSuwar(
    allSuwar: AllSuwar,
    progress: Float = 0f,
    progressTimer: Float = 0f,
    duration: Long = 0L,
    onProgress: (Float) -> Unit,
    isAudioPlaying: Boolean = false,
    currentPlayingAudio: Audio = Audio(),
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
            val suwarList = allSuwar.suwar
            items(suwarList) { surah ->
                Column(
                    modifier = Modifier
                        .surfaceModifier(shape = RoundedCornerShape(8.dp))
                        .clickable {
                            onItemClick((surahNameToIdMap[surah.name] ?: 0) - 1)
                        }
                        .padding(16.dp)

                ) {
                    Text(
                        text = "${surahNameToIdMap[surah.name]} - سورة ${surah.name}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
