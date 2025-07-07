package com.alqiran.quraanapp.ui.reciters_package.viewModel

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
class RecitersViewModel @Inject constructor(
    private val repo: Repository
): ViewModel() {

    private val _state = MutableStateFlow<RecitersState>(RecitersState.Loading)
    val state = _state.asStateFlow()

    fun fetchReciters() {
        _state.value = RecitersState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val allReciters = repo.getAllReciters()
                _state.value = RecitersState.Success(allReciters)
            } catch (e: Exception) {
                _state.value = RecitersState.Error(e.message.toString())
            }
        }
    }

}