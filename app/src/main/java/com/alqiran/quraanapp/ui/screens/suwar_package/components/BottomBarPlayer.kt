package com.alqiran.quraanapp.ui.screens.suwar_package.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alqiran.quraanapp.data.datasources.remote.model.Audio
import com.alqiran.quraanapp.ui.screens.suwar_package.ArtistInfo

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