package com.alqiran.quraanapp.ui.screens.suwar_package.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alqiran.quraanapp.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuwarViewModel @Inject constructor(
    private val repo: Repository
): ViewModel() {

    private val _state = MutableStateFlow<SuwarState>(SuwarState.Loading)
    val state = _state.asStateFlow()

    fun fetchSuwar() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = SuwarState.Loading

            try {
                val allSuwar = repo.getAllSuwar()
                _state.value = SuwarState.Success(allSuwar)
            } catch (e: Exception) {
                _state.value = SuwarState.Error(e.message.toString())
            }
        }
    }

}