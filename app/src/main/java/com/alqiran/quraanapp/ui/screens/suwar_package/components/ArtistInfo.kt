package com.alqiran.quraanapp.ui.screens.suwar_package.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alqiran.quraanapp.data.datasources.remote.model.Audio

@Composable
fun ArtistInfo(modifier: Modifier = Modifier, audio: Audio) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text = audio.surah,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Clip,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = audio.reciter,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
        }
    }
}