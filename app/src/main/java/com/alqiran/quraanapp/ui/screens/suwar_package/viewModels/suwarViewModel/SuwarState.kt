package com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel

import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.SuwarExist

sealed class SuwarState {
    data object Loading: SuwarState()

    data class SuccessFetching(val suwarExist: List<SuwarExist>): SuwarState()
    data class Success(val suwarExist: List<SuwarExist>): SuwarState()

    data class Error(val message: String): SuwarState()
}