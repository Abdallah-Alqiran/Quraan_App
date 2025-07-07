package com.alqiran.quraanapp.ui.screens.reciters_package.viewModel

import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.AllReciters


sealed class RecitersState {

    data object Loading: RecitersState()

    data class Success(val reciters: AllReciters): RecitersState()

    data class Error(val message: String): RecitersState()
}