package com.alqiran.quraanapp.ui.screens.reciters_package

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alqiran.quraanapp.ui.components.loading_and_failed.FailedLoadingScreen
import com.alqiran.quraanapp.ui.components.loading_and_failed.LoadingProgressIndicator
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.Reciter
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.ui.components.modifiers.surfaceModifier
import com.alqiran.quraanapp.ui.screens.reciters_package.viewModel.RecitersState
import com.alqiran.quraanapp.ui.screens.reciters_package.viewModel.RecitersViewModel


@Composable
fun RecitersScreen(onReciterClick:(riwayatReciter: List<RecitersMoshafReading>, reciterName: String) -> Unit) {

    val recitersViewModel = hiltViewModel<RecitersViewModel>()
    val state by recitersViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        recitersViewModel.fetchReciters()
    }

    when (state) {
        is RecitersState.Error -> {
            FailedLoadingScreen(errorMessage = (state as RecitersState.Error).message) {
                recitersViewModel.fetchReciters()
            }
            Log.d("Al-qiran", (state as RecitersState.Error).message)
        }

        RecitersState.Loading -> LoadingProgressIndicator()
        is RecitersState.Success -> {
            val allRecitersSortedByName = (state as RecitersState.Success).reciters.reciters.sortedBy { it.name }
            PrintAllReciters(allReciters = allRecitersSortedByName, onReciterClick)
        }
    }

}

@Composable
fun PrintAllReciters(allReciters: List<Reciter>, onReciterClick:(riwayatReciter: List<RecitersMoshafReading>, reciterName: String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(allReciters) { reciter ->
            Row(
                modifier = Modifier
                    .surfaceModifier()
                    .clickable {
                        onReciterClick(reciter.moshaf, reciter.name)
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = "الشيخ ${reciter.name}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier

                )

            }
//            Column {
//                Row {
//                    Text(
//                        reciter.id.toString(),
//                    )
//                    Text(reciter.name)
//                }
//                Text(reciter.letter)
//                Text(reciter.date)
//            }
//            (reciter.moshaf).forEach { mf ->
//                Column {
//                    Row {
//                        Text(
//                            mf.id.toString(),
//                        )
//                        Text(mf.name)
//                    }
//                    Text(mf.moshafType.toString())
//                    Text("Server ${mf.server}")
//                    Text("surah List: ${mf.surahList}")
//                    Text("surah Total: ${mf.surahTotal}")
//
//                }
//            }
        }
    }
}