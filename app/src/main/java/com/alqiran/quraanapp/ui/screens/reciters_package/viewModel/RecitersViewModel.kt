package com.alqiran.quraanapp.ui.screens.reciters_package.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.AllReciters
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

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var _allReciters = AllReciters(emptyList())

    fun fetchReciters() {
        _state.value = RecitersState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _allReciters = repo.getAllReciters()
                _state.value = RecitersState.Success(_allReciters)
            } catch (e: Exception) {
                _state.value = RecitersState.Error(e.message.toString())
            }
        }
    }

    fun onSearchTextChange(newText: String) {
        _state.value = RecitersState.Loading

        _searchText.value = newText
        val filteredReciters = _allReciters.reciters.filter { reciter ->
            reciter.name.contains(newText, ignoreCase = true)
        }
        _state.value = RecitersState.Success(AllReciters(filteredReciters))
    }
}