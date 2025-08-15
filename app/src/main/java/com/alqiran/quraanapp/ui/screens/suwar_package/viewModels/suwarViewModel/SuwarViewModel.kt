package com.alqiran.quraanapp.ui.screens.suwar_package.viewModels.suwarViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.SuwarExist
import com.alqiran.quraanapp.ui.utils.surahIdToNameMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SuwarViewModel() : ViewModel() {

    private val _state = MutableStateFlow<SuwarState>(SuwarState.Loading)
    val state = _state.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var _suwarExist: List<SuwarExist> = emptyList()

    private val _recitersMoshafReading = MutableStateFlow<RecitersMoshafReading?>(null)

    fun setRecitersMoshafReading(recitersMoshafReading: RecitersMoshafReading) {
        _state.value = SuwarState.Loading
        _searchText.value = ""
        _suwarExist = emptyList()
        _recitersMoshafReading.value = null

        _recitersMoshafReading.value = recitersMoshafReading
        viewModelScope.launch {
            createAllExistingSuwar()
        }
    }

    private suspend fun createAllExistingSuwar() {
        _state.value = SuwarState.Loading

        val surahNumber: List<Int> =
            _recitersMoshafReading.value?.surahList?.split(",")?.map { it.toInt() } ?: emptyList()
        var counter = 0;

        val list: List<SuwarExist> = surahNumber.map { number ->
            SuwarExist(
                id = counter++,
                surahNumber = number,
                name = surahIdToNameMap[number] ?: "",
            )
        }
        _suwarExist = list

        _state.value = SuwarState.SuccessFetching(_suwarExist)
        delay(1000)
        _state.value = SuwarState.Success(_suwarExist)
    }

    fun onSearchTextChange(newText: String) {
        _state.value = SuwarState.Loading

        _searchText.value = newText
        val filteredSuwar = _suwarExist.filter { surah ->
            surah.name.contains(newText, ignoreCase = true)
        }
        _state.value = SuwarState.Success(filteredSuwar)
    }
}