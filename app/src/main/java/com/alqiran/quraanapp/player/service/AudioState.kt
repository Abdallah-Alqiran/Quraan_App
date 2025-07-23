package com.alqiran.quraanapp.player.service

sealed class AudioState {
    object Initial: AudioState()
    data class Ready(val duration: Long): AudioState()
    data class Progress(val progress: Long): AudioState()
    data class Buffering(val progress: Long): AudioState()
    data class Playing(val isPlaying: Boolean): AudioState()
    data class CurrentPlaying(val mediaItemIndex: Int): AudioState()
}