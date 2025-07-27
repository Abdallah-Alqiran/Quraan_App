package com.alqiran.quraanapp.ui.screens.suwar_package

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel.SuwarState
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel.SuwarViewModel
import kotlin.math.floor


@Composable
fun SuwarScreen(suwarListAndServer: RecitersMoshafReading, reciterName: String) {

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
                allSuwar = (state as SuwarState.Success).allSuwar,
                reciterName
            )
        }
    }
}
/*    progress: Float,
    onProgress: (Float) -> Unit,
    isAudioPlaying: Boolean,
    currentPlayingAudio: Audio,
    audiList: List<Audio>,
    onStart: () -> Unit,
    onItemClick: (Int) -> Unit,
    onNext: () -> Unit,
*/

@Composable
fun PrintAllSuwar(
    suwarListAndServer: RecitersMoshafReading,
    allSuwar: AllSuwar,
    reciterName: String,
    progress: Float = 0f,
    onProgress: () -> Unit = {},
    isAudioPlaying: Boolean = false,
    currentPlayingAudio: Audio = Audio(),
    audiList: List<Audio> = listOf(),
    onStart: () -> Unit = {},
    onItemClick: (Int) -> Unit = {},
    onNext: () -> Unit = {},
) {
    val audioList = mutableListOf<Audio>()

    val suwarListNumber = suwarListAndServer.surahList.split(",")

    for (index in 0..<suwarListNumber.size) {
        audioList.add(
            Audio(
                surahNumber = index.toString(),
                server = suwarListAndServer.server,
                surah = allSuwar.suwar[suwarListNumber[index].toInt() - 1].name,
                reciter = reciterName,
                duration = 0
            )
        )
    }
    Scaffold(
        bottomBar = {
            BottomBarPlayer(
                progress = progress,
                onProgress = { onProgress() },
                audio = audioList[0],
                isAudioPlaying = isAudioPlaying,
                onStart = {},
                onNext = {},
                onPrevious = {}
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

                AudioItem(
                    audio = audioList[index]
                ) {
                    onItemClick(index)
                }

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)

                ) {

                    Text(
                        text = "${suwarListNumber[index]} - سورة ${allSuwar.suwar[suwarListNumber[index].toInt() - 1].name}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                    )

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = timeStampToDuration(audioList[index].duration))
                        Slider(
                            value = 20f, onValueChange = {}, valueRange = 0f..100f,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        )

                        Text(text = timeStampToDuration(audioList[index].duration))
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            // TODO NEXT
                        }) {
                            Icon(
                                painter = painterResource(ic_skip_next),
                                contentDescription = "Go Next"
                            )
                        }

                        IconButton(onClick = {
                            // TODO Play Pause

                        }) {
                            Icon(
                                painter = if (true) painterResource(ic_play) else painterResource(
                                    ic_pause
                                ),
                                contentDescription = "Play_Pause"
                            )
                        }

                        IconButton(onClick = {
                            // TODO Previous
                        }) {
                            Icon(
                                painter = painterResource(ic_skip_previous),
                                contentDescription = "Go Previous"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AudioItem(audio: Audio, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                onItemClick()
            }
    ) { }
}

@Composable
fun BottomBarPlayer(
    progress: Float,
    onProgress: (Float) -> Unit,
    audio: Audio,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    BottomAppBar(

    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArtistInfo(audio = audio, modifier = Modifier.weight(1f))

                MediaPlayerController(isAudioPlaying, onStart, onNext, onPrevious)

                Slider(
                    value = progress,
                    onValueChange = {
                        onProgress(it)
                    },
                    valueRange = 0f..100f
                )
            }
        }
    }
}


private fun timeStampToDuration(position: Long): String {
    val totalSecond = floor(position / 1E3).toInt()
    val minutes = totalSecond / 60
    val remainingSeconds = totalSecond - (minutes * 60)
    return if (position < 0) "--:--"
    else "%d:%02d".format(minutes, remainingSeconds)
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

        Column() {
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


@Composable
fun PlayerIconItem(
    modifier: Modifier = Modifier,
    icon: Int,
    borderStroke: BorderStroke? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        border = borderStroke,
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        contentColor = color,
        color = backgroundColor
    ) {
        Box(modifier = modifier.padding(4.dp)) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "image"
            )
        }
    }
}


@Composable
fun MediaPlayerController(
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
) {

    Icon(
        painter = painterResource(ic_skip_previous),
        contentDescription = "Go Next",
        modifier = Modifier.clickable {
            onPrevious()
        }
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(56.dp)
            .padding(4.dp)
    ) {
        PlayerIconItem(icon = if (isAudioPlaying) ic_pause else ic_play) {
            onStart()
        }
        Spacer(modifier = Modifier.size(8.dp))

        Icon(
            painter = painterResource(ic_skip_next),
            contentDescription = "Go Next",
            modifier = Modifier.clickable {
                onNext()
            }
        )

    }
}
