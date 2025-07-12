package com.alqiran.quraanapp.ui.screens.riwayat_package

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.ui.components.modifiers.surfaceModifier

@Composable
fun RiwayatScreen(
    riwayatReciter: List<RecitersMoshafReading>,
    reciterName: String,
    onRiwayaClick: (RecitersMoshafReading) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                "للقارئ الشيخ $reciterName",
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(riwayatReciter) { riwaya ->
            Row(
                modifier = Modifier
                    .surfaceModifier()
                    .clickable {
                        onRiwayaClick(riwaya)
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = "رواية ${riwaya.name}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier

                )

            }

        }
    }
}