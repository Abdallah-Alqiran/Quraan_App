package com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.Suwar
import com.alqiran.quraanapp.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SuwarViewModel @Inject constructor(
    private val repo: Repository,
) : ViewModel() {

    private val _state = MutableStateFlow<SuwarState>(SuwarState.Loading)
    val state = _state.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var _allSuwar = AllSuwar(emptyList())

    fun fetchSuwar() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = SuwarState.Loading

            try {
                _allSuwar = repo.getAllSuwar()
                _state.value = SuwarState.Success(_allSuwar)
            } catch (e: Exception) {
                _state.value = SuwarState.Error(e.message.toString())
            }
        }
    }

    fun onSearchTextChange(newText: String) {
        _state.value = SuwarState.Loading

        _searchText.value = newText
        val filteredSuwar = _allSuwar.suwar.filter { surah ->
            surah.name.contains(newText, ignoreCase = true)
        }
        _state.value = SuwarState.Success(AllSuwar(filteredSuwar))
    }

}