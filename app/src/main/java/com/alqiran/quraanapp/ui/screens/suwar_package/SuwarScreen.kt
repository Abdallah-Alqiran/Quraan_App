package com.alqiran.quraanapp.ui.screens.suwar_package

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar
import com.alqiran.quraanapp.ui.components.loading_and_failed.FailedLoadingScreen
import com.alqiran.quraanapp.ui.components.loading_and_failed.LoadingProgressIndicator
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alqiran.quraanapp.R
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModel.PlaySuwarViewModel
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModel.SuwarState
import com.alqiran.quraanapp.ui.screens.suwar_package.viewModel.SuwarViewModel
import com.alqiran.quraanapp.ui.utils.RequestNotificationPermission


@Composable
fun SuwarScreen(suwarListAndServer: RecitersMoshafReading, reciterName: String) {

    RequestNotificationPermission()

    val suwarViewModel = hiltViewModel<SuwarViewModel>()
    val playSuwarViewModel = viewModel<PlaySuwarViewModel>()
    val state by suwarViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        suwarViewModel.fetchSuwar()
    }

    when (state) {
        is SuwarState.Error -> {
            FailedLoadingScreen((state as SuwarState.Error).message) {
                suwarViewModel.fetchSuwar()
            }
        }

        SuwarState.Loading -> {
            LoadingProgressIndicator()
        }

        is SuwarState.Success -> {
            PrintAllSuwar(
                suwarListAndServer = suwarListAndServer,
                allSuwar = (state as SuwarState.Success).allSuwar,
                playSuwarViewModel,
                reciterName
            )
        }
    }
}

@Composable
fun PrintAllSuwar(
    suwarListAndServer: RecitersMoshafReading,
    allSuwar: AllSuwar,
    playSuwarViewModel: PlaySuwarViewModel,
    reciterName: String
) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val suwarListNumber = suwarListAndServer.surahList.split(",")
        items(suwarListNumber.size) { index ->
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)

            ) {

                Text(
                    text = "${suwarListNumber[index]} - سورة ${allSuwar.suwar[suwarListNumber[index].toInt() - 1].name}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "02:12")
                    Slider(
                        value = 20f, onValueChange = {}, valueRange = 0f..100f,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                    Text(text = "05:41")
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        // TODO NEXT
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_skip_next),
                            contentDescription = "Go Next"
                        )
                    }

                    IconButton(onClick = {
                        // TODO Play Pause

                    }) {
                        Icon(
                            painter = if (true) painterResource(R.drawable.ic_play) else painterResource(
                                R.drawable.ic_pause
                            ),
                            contentDescription = "Play_Pause"
                        )
                    }

                    IconButton(onClick = {
                        // TODO Previous
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_skip_previous),
                            contentDescription = "Go Previous"
                        )
                    }
                }
            }
        }
    }
}
