package com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel

sealed class AudioEvents {
    object PlayPause : AudioEvents()
    data class SelectedAudioChange(val index: Int) : AudioEvents()
    data class SeekTo(val position: Float) : AudioEvents()
    object SeekToNext : AudioEvents()
    object Backward : AudioEvents()
    object Forward : AudioEvents()
    data class UpdateProgress(val newProgress: Float) : AudioEvents()
}
