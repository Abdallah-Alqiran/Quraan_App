package com.alqiran.quraanapp.ui.reciters_package

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.AllReciters
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.Reciter
import com.alqiran.quraanapp.ui.reciters_package.viewModel.RecitersState
import com.alqiran.quraanapp.ui.reciters_package.viewModel.RecitersViewModel


@Composable
fun RecitersScreen() {

    val recitersViewModel = hiltViewModel<RecitersViewModel>()
    val recitersState by recitersViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        recitersViewModel.fetchReciters()
    }

    when (recitersState) {
        is RecitersState.Error -> {
            Log.d("Al-qiran", (recitersState as RecitersState.Error).message)
        }

        RecitersState.Loading -> Unit
        is RecitersState.Success -> {
            val allRecitersSortedByName = (recitersState as RecitersState.Success).reciters.reciters.sortedBy { it.name }
            PrintAllReciters(allReciters = allRecitersSortedByName)
        }
    }

}

@Composable
fun PrintAllReciters(allReciters: List<Reciter>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(allReciters) { reciter ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ), shape = CircleShape
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {

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