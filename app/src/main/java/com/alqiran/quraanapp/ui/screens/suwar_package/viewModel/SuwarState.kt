package com.alqiran.quraanapp.ui.screens.suwar_package.viewModel

import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar

sealed class SuwarState {
    data object Loading: SuwarState()

    data class Success(val allSuwar: AllSuwar): SuwarState()

    data class Error(val message: String): SuwarState()
}