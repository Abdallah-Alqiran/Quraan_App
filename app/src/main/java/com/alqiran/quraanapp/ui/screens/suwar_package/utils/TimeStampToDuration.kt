package com.alqiran.quraanapp.ui.screens.suwar_package.utils

import android.util.Log
import kotlin.math.floor

fun timeStampToDuration(position: Long): String {

    Log.d("Al-qiran", "time stamp: $position")

    if (position < 0) return "--:--"

    val totalSeconds = floor(position / 1E3).toInt()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%d:%02d".format(minutes, seconds)
    }
}
