package com.alqiran.quraanapp.ui.screens.suwar_package

import android.content.Intent
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.ui.components.loading_and_failed.FailedLoadingScreen
import com.alqiran.quraanapp.ui.components.loading_and_failed.LoadingProgressIndicator
import com.alqiran.quraanapp.data.datasources.remote.model.Audio
import com.alqiran.quraanapp.ui.screens.suwar_package.model.SuwarExist
import com.alqiran.quraanapp.player.service.AudioService
import com.alqiran.quraanapp.ui.components.modifiers.surfaceModifier
import com.alqiran.quraanapp.ui.components.search.DefaultSearchField
import com.alqiran.quraanapp.ui.screens.suwar_package.components.BottomBarPlayer
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel.AudioEvents
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel.AudioViewModel
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel.SuwarState
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel.SuwarViewModel


@Composable
fun SuwarScreen(suwarListAndServer: RecitersMoshafReading, reciterName: String) {

    val suwarViewModel = viewModel<SuwarViewModel>()
    val audioViewModel = hiltViewModel<AudioViewModel>()
    val state by suwarViewModel.state.collectAsStateWithLifecycle()
    val searchText by suwarViewModel.searchText.collectAsState()

    LaunchedEffect(suwarListAndServer) {
        suwarViewModel.setRecitersMoshafReading(suwarListAndServer)
    }

    var isServiceRunning = false

    val context = LocalContext.current

    when (state) {
        is SuwarState.Error -> {
            FailedLoadingScreen((state as SuwarState.Error).message) {
            }
        }

        SuwarState.Loading -> {
            LoadingProgressIndicator()
        }

        is SuwarState.SuccessFetching -> {
            LoadingProgressIndicator()

            val suwarList = (state as SuwarState.SuccessFetching).suwarExist

            val audioList =
                suwarList.map { surah ->
                    Audio(
                        surahNumber = surah.surahNumber.toString(),
                        server = suwarListAndServer.server,
                        surah = surah.name,
                        reciter = reciterName,
                    )
                }
            audioViewModel.setAllAudioData(audioList)
        }

        is SuwarState.Success -> {

            val suwarList = (state as SuwarState.Success).suwarExist

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
                    allSuwar = suwarList,
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
                        audioViewModel.onAudioEvents(AudioEvents.SelectedAudioChange(it))
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
    allSuwar: List<SuwarExist>,
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
            items(allSuwar) { surah ->
                Column(
                    modifier = Modifier
                        .surfaceModifier(shape = RoundedCornerShape(8.dp))
                        .clickable {
                            onItemClick(surah.id)
                        }
                        .padding(16.dp)

                ) {
                    Text(
                        text = "${surah.surahNumber} - سورة ${surah.name}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
