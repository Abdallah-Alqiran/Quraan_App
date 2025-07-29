package com.alqiran.quraanapp.ui.components.modifiers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp



@Composable
fun Modifier.surfaceModifier(shape: RoundedCornerShape = CircleShape): Modifier {
    return this
        .padding(8.dp)
        .fillMaxWidth()
        .clip(shape)
        .border(
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primaryContainer
            ), shape = shape
        )
        .background(MaterialTheme.colorScheme.surface)

}