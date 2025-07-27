package com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.audioViewModel


sealed class AudioUiState {

    object Initial: AudioUiState()
    object Ready: AudioUiState()
}