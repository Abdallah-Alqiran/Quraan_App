package com.alqiran.quraanapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.alqiran.quraanapp.ui.screens.reciters_package.RecitersScreen
import com.alqiran.quraanapp.ui.screens.riwayat_package.RiwayatScreen
import com.alqiran.quraanapp.ui.screens.suwar_package.SuwarScreen

@Composable
fun AppNavDisplay(modifier: Modifier = Modifier, backStack: NavBackStack,screenWithValue: MutableState<Pair<String, String>>) {


    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = { key ->
            when (key) {
                is RecitersScreenRoute -> {
                    NavEntry(key = key) {
                        screenWithValue.value = "reciters_screen" to "اختر القارئ"

                        RecitersScreen { riwayatReciter, reciterName ->
                            backStack.add(RiwayatScreenRoute(riwayatReciter, reciterName))
                        }
                    }
                }

                is RiwayatScreenRoute -> {
                    NavEntry(key = key) {
                        screenWithValue.value = "riwayat_screen" to "اختر الرواية"

                        RiwayatScreen(
                            riwayatReciter = key.riwayatReciter,
                            reciterName = key.reciterName
                        ) {
                            backStack.add(SuwarScreenRoute(suwar = it))
                        }
                    }
                }

                is SuwarScreenRoute -> {
                    NavEntry(key = key) {
                        screenWithValue.value = "suwar_screen" to "استمع..."

                        SuwarScreen(key.suwar)
                    }
                }
                else -> throw RuntimeException("Invalid NavKey")
            }
        }
    )
}