package com.alqiran.quraanapp.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.ui.screens.reciters_package.RecitersScreen
import com.alqiran.quraanapp.ui.screens.riwayat_package.RiwayatScreen
import kotlin.reflect.typeOf

@Composable
fun AppNavHost(modifier: Modifier = Modifier, navController: NavHostController, rememberString: MutableState<String>) {

    NavHost(
        navController = navController,
        startDestination = RecitersScreenRoute,
        modifier = modifier
    ) {
        composable<RecitersScreenRoute> {
            rememberString.value = "reciters_screen"

            RecitersScreen { riwayatReciter, reciterName ->
                navController.navigate(RiwayatScreenRoute(riwayatReciter, reciterName))
            }
        }

        composable<RiwayatScreenRoute>(
            typeMap = mapOf(
                typeOf<List<RecitersMoshafReading>>() to CustomNavType.riwayatType,
            )
        ) {
            rememberString.value = "riwayat_screen"

            val arguments = it.toRoute<RiwayatScreenRoute>()
            RiwayatScreen(
                riwayatReciter = arguments.riwayatReciter,
                reciterName = arguments.reciterName
            )
        }

    }

}