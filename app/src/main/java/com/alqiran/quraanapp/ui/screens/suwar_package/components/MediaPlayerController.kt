package com.alqiran.quraanapp.ui.screens.suwar_package.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alqiran.quraanapp.R.drawable.ic_pause
import com.alqiran.quraanapp.R.drawable.ic_play
import com.alqiran.quraanapp.R.drawable.ic_skip_next
import com.alqiran.quraanapp.R.drawable.ic_skip_previous
import com.alqiran.quraanapp.ui.screens.suwar_package.utils.PlayerIconItem


@Composable
fun MediaPlayerController(
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(56.dp)
            .padding(4.dp)
    ) {

        PlayerIconItem(icon = ic_skip_next, contentDescription = "Go Next") {
            onNext()
        }
        Spacer(modifier = Modifier.size(8.dp))

        PlayerIconItem(icon = if (isAudioPlaying) ic_pause else ic_play, contentDescription = "Play Pause") {
            onStart()
        }

        Spacer(modifier = Modifier.size(8.dp))


        PlayerIconItem(icon = ic_skip_previous, contentDescription = "Go Previous") {
            onPrevious()
        }
    }
}