@file:OptIn(SavedStateHandleSaveableApi::class)

package com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.alqiran.quraanapp.data.datasources.remote.model.Audio
import com.alqiran.quraanapp.player.service.AudioServiceHandler
import com.alqiran.quraanapp.player.service.AudioState
import com.alqiran.quraanapp.player.service.PlayerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject


private val audioDummy = Audio(
    surahNumber = "", server = "", surah = "000", reciter = "", duration = 0L
)


@HiltViewModel
class AudioViewModel @Inject constructor(
    private val audioServiceHandler: AudioServiceHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var duration by savedStateHandle.saveable { mutableLongStateOf(0L) }
    var progress by savedStateHandle.saveable { mutableFloatStateOf(0f) }
    var progressString by savedStateHandle.saveable { mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
    var currentSelectedAudio by savedStateHandle.saveable { mutableStateOf(audioDummy) }
    var audioList by savedStateHandle.saveable { mutableStateOf(listOf<Audio>()) }

    private val _state = MutableStateFlow<AudioUiState>(AudioUiState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            audioServiceHandler.audioState.collectLatest { audioState ->
                when (audioState) {
                    AudioState.Initial -> _state.value = AudioUiState.Initial
                    is AudioState.Buffering -> calculateProgressValue(audioState.progress)
                    is AudioState.Playing -> isPlaying = audioState.isPlaying
                    is AudioState.Progress -> calculateProgressValue(audioState.progress)
                    is AudioState.CurrentPlaying -> currentSelectedAudio =
                        audioList[audioState.mediaItemIndex]

                    is AudioState.Ready -> {
                        duration = audioState.duration
                        _state.value = AudioUiState.Ready
                    }
                }
            }
        }
    }

    fun getAllAudioData(allAudio: List<Audio>) {
        audioList = allAudio
        setMediaItems()
    }

    private fun setMediaItems() {
        audioList.map { audio ->
            MediaItem.Builder()
                .setUri("${audio.server}${audio.surahNumber.padStart(3, '0')}.mp3")
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(audio.reciter)
                        .setDisplayTitle(audio.surah)
                        .setSubtitle(audio.server)
                        .build()
                )
                .build()
        }.also {
            audioServiceHandler.setMediaItemList(it)
        }
    }

    private fun calculateProgressValue(currentProgress: Long) {
        progress = if (currentProgress > 0) ((currentProgress.toFloat() / duration) * 100f) else 0f
        progressString = formatDuration(currentProgress)
    }

    private fun formatDuration(duration: Long): String {
        val minute = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (minute) - minute * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
        return String.format(Locale.getDefault(), "%02d:%02d", minute, seconds)
    }


    fun onUiEvents(audioEvent: AudioEvents) = viewModelScope.launch{
        when(audioEvent) {
            AudioEvents.Backward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            AudioEvents.Forward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            AudioEvents.PlayPause -> audioServiceHandler.onPlayerEvents(PlayerEvent.PlayPause)
            is AudioEvents.SeekTo ->  audioServiceHandler.onPlayerEvents(playerEvent = PlayerEvent.SeekTo, seekPosition = ((duration * audioEvent.position) / 100f).toLong())
            AudioEvents.SeekToNext ->  audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
            is AudioEvents.SelectedAudioChange ->  audioServiceHandler.onPlayerEvents(PlayerEvent.SelectedAudioChange, selectedAudioIndex = audioEvent.index)
            is AudioEvents.UpdateProgress ->  audioServiceHandler.onPlayerEvents(PlayerEvent.UpdateProgress(audioEvent.newProgress))
        }
    }
}
//  val surahUrl = server + suwarList[index].padStart(3, '0')+ ".mp3"
