package com.alqiran.quraanapp.ui.screens.suwar_package

import android.content.ComponentName
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar
import com.alqiran.quraanapp.data.services.QuranPlayerService
import com.alqiran.quraanapp.ui.components.loading_and_failed.FailedLoadingScreen
import com.alqiran.quraanapp.ui.components.loading_and_failed.LoadingProgressIndicator
import com.alqiran.quraanapp.ui.components.modifiers.surfaceModifier
import androidx.lifecycle.lifecycleScope
import com.alqiran.quraanapp.R
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModel.SuwarState
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModel.SuwarViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
                allSuwar = (state as SuwarState.Success).allSuwar
            )
        }
    }
}

@Composable
fun PrintAllSuwar(suwarListAndServer: RecitersMoshafReading, allSuwar: AllSuwar) {

    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val isPlaying = MutableStateFlow(false)
    val maxDuration = MutableStateFlow(0f)
    val currentDuration = MutableStateFlow(0f)
    val currentSurah = MutableStateFlow("surahUrl") // TODO should send the surah url
    var service: QuranPlayerService? = null
    var isBound = false


    val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?, binder: IBinder?
        ) {
            service = (binder as QuranPlayerService.QuranBinder).getService()
            lifecycleOwner.lifecycleScope.launch {
                binder.isPlaying().collectLatest {
                    isPlaying.value = it
                }
            }
            lifecycleOwner.lifecycleScope.launch {
                binder.maxDuration().collectLatest {
                    maxDuration.value = it
                }
            }
            lifecycleOwner.lifecycleScope.launch {
                binder.currentDuration().collectLatest {
                    currentDuration.value = it
                }
            }
            lifecycleOwner.lifecycleScope.launch {
                binder.isPlaying().collectLatest {
                    isPlaying.value = it
                }
            }
            lifecycleOwner.lifecycleScope.launch {
                binder.getCurrentSurah().collectLatest {
                    currentSurah.value = it
                }
            }
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val suwarListNumber = suwarListAndServer.surahList.split(",")
        items(suwarListNumber.size) { index ->
            Column(modifier = Modifier
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
                val max by maxDuration.collectAsState()
                val current by currentDuration.collectAsState()
                val isPlaying by isPlaying.collectAsState()

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
                    Text(text = current.div(1000).toString())
                    Slider(value = current, onValueChange = {}, valueRange = 0f..max)
                    Text(text = max.div(1000).toString())
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        mediaPlayer?.release()

                        if (suwarListNumber.size - 1 == index) {
                            playSurah(suwarListNumber, 0, suwarListAndServer.server)
                        } else {
                            playSurah(suwarListNumber, index + 1, suwarListAndServer.server)
                        }


                        // TODO
                        service?.next()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_skip_next),
                            contentDescription = "Go Next"
                        )
                    }

                    IconButton(onClick = {
                        mediaPlayer?.release()
                        mediaPlayer = playSurah(suwarListNumber, index, suwarListAndServer.server)

                        // TODO
                        service?.playPause()
                    }) {
                        Icon(
                            painter = if (isPlaying) painterResource(R.drawable.ic_play) else painterResource(R.drawable.ic_pause),
                            contentDescription = "Play_Pause"
                        )
                    }

                    IconButton(onClick = {

                        mediaPlayer?.release()

                        if (index == 0) {
                            playSurah(suwarListNumber, suwarListNumber.size - 1, suwarListAndServer.server)
                        } else {
                            playSurah(suwarListNumber, index - 1, suwarListAndServer.server)
                        }

                        // TODO
                        service?.prev()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_skip_previous),
                            contentDescription = "Go Previous"
                        )
                    }
                }
            }
        }
    }
}

fun playSurah(suwarList: List<String>, index: Int, server: String): MediaPlayer {
    val surahUrl = server + suwarList[index].padStart(3, '0') + ".mp3"

    Log.d("Al-qiran", surahUrl)
    return MediaPlayer().apply {
        setDataSource(surahUrl)
        setOnPreparedListener { it.start() }
        setOnCompletionListener {
            if (suwarList.size - 1 == index) {
                Log.d("Al-qiran", "$surahUrl $index ${suwarList.size} ${suwarList[index]}")
                playSurah(suwarList, 0, server)
            } else {
                Log.e("Al-qiran", "$surahUrl $index ${suwarList.size} ${suwarList[index]}")
                playSurah(suwarList, index + 1, server)
            }
        }
        setOnErrorListener { _, _, _ ->
            Log.d(
                "Al-qiran",
                "entered set on Error $surahUrl $index ${suwarList.size} ${suwarList[index]}"
            )
            true
        }
        prepareAsync()
    }
}