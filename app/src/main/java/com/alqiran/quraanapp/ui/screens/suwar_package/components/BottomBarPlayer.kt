package com.alqiran.quraanapp.ui.screens.suwar_package.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alqiran.quraanapp.data.datasources.remote.model.Audio
import com.alqiran.quraanapp.theme.QuraanAppTheme
import com.alqiran.quraanapp.ui.screens.suwar_package.utils.timeStampToDuration

@Composable
fun BottomBarPlayer(
    progress: Float,
    progressTimer: Float,
    duration: Long,
    onProgress: (Float) -> Unit,
    audio: Audio,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        containerColor = MaterialTheme.colorScheme.surfaceTint
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            ArtistInfo(audio = audio, modifier = Modifier.fillMaxWidth())

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                MediaPlayerController(isAudioPlaying, onStart, onNext, onPrevious)

                Text(
                    text = timeStampToDuration((progressTimer).toLong()),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Slider(
                    value = progress,
                    onValueChange = { onProgress(it) },
                    valueRange = 0f..100f,
                    modifier = Modifier
                        .weight(2f)
                        .height(1.dp)
                        .padding(horizontal = 4.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primaryContainer,
                        activeTrackColor = Color.Transparent,
                        inactiveTrackColor = Color.Transparent,
                        disabledThumbColor = Color.Transparent,
                        disabledActiveTrackColor = Color.Transparent,
                        disabledInactiveTrackColor = Color.Transparent,
                        disabledInactiveTickColor = Color.Transparent,
                        disabledActiveTickColor = Color.Transparent,
                        activeTickColor = MaterialTheme.colorScheme.primaryContainer,
                        inactiveTickColor = MaterialTheme.colorScheme.primary
                    ),
                    steps = 99
                )
                Text(
                    text = timeStampToDuration(duration),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun Prev() {
    QuraanAppTheme {
        BottomBarPlayer(
            progress = 50f,
            progressTimer = 0f,
            onProgress = { },
            duration = 0L,
            audio = Audio(),
            isAudioPlaying = true,
            onStart = {},
            onNext = {},
            onPrevious = {}
        )
    }
}