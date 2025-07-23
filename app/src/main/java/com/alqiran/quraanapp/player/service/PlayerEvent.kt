package com.alqiran.quraanapp.player.service

sealed class PlayerEvent {
    object PlayPause: PlayerEvent()
    object SelectedAudioChange: PlayerEvent()
    object Backward: PlayerEvent()
    object Forward: PlayerEvent()
    object SeekTo: PlayerEvent()
    object Stop: PlayerEvent()
    data class UpdateProgress(val newProgress: Float): PlayerEvent()
}