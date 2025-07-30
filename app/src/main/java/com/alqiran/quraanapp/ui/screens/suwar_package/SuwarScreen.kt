package com.alqiran.quraanapp.ui.screens.suwar_package

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startForegroundService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar
import com.alqiran.quraanapp.ui.components.loading_and_failed.FailedLoadingScreen
import com.alqiran.quraanapp.ui.components.loading_and_failed.LoadingProgressIndicator
import com.alqiran.quraanapp.R.drawable.ic_pause
import com.alqiran.quraanapp.R.drawable.ic_play
import com.alqiran.quraanapp.R.drawable.ic_skip_next
import com.alqiran.quraanapp.R.drawable.ic_skip_previous
import com.alqiran.quraanapp.data.datasources.remote.model.Audio
import com.alqiran.quraanapp.player.service.AudioService
import com.alqiran.quraanapp.ui.components.modifiers.surfaceModifier
import com.alqiran.quraanapp.ui.screens.suwar_package.components.BottomBarPlayer
import com.alqiran.quraanapp.ui.screens.suwar_package.utils.PlayerIconItem
import com.alqiran.quraanapp.ui.screens.suwar_package.utils.timeStampToDuration
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel.AudioEvents
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel.AudioViewModel
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel.SuwarState
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel.SuwarViewModel
import kotlin.math.floor


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
                        surahNumber = surahNumber, // Use the actual surah number from API
                        server = suwarListAndServer.server,
                        surah = (state as SuwarState.Success).allSuwar.suwar[surahNumber.toInt() - 1].name,
                        reciter = reciterName,
                        duration = 0
                    )
                }
            }

            LaunchedEffect(audioList) {
                Log.e("Al-qiran", "Audio List: $audioList")
                audioViewModel.setAllAudioData(audioList)
            }

            PrintAllSuwar(
                suwarListAndServer = suwarListAndServer,
                allSuwar = (state as SuwarState.Success).allSuwar,
                reciterName = reciterName,
                progress = audioViewModel.progress,
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
                    Log.d("Al-qiran", "from onItemClick in SuwarScreen: surahNumber: $surahNumber index: $surahIndex the size: ${suwarListNumber.size}")
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
                onProgress = onProgress,
                audio = audioList[0],
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


@Composable
fun ArtistInfo(modifier: Modifier = Modifier, audio: Audio) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerIconItem(
            icon = ic_play,
            borderStroke = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
        ) {}

        Spacer(modifier = Modifier.size(4.dp))

        Column {
            Text(
                text = audio.surah,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = audio.reciter,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
        }
    }
}