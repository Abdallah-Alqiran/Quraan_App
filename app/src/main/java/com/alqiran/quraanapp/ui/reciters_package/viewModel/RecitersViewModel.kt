package com.alqiran.quraanapp.ui.reciters_package.viewModel

import androidx.lifecycle.ViewModel
import com.alqiran.quraanapp.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecitersViewModel @Inject constructor(
    repo: Repository
): ViewModel() {



}