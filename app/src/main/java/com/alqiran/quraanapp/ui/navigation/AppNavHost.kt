package com.alqiran.quraanapp.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import com.alqiran.quraanapp.ui.reciters_package.RecitersScreen
import kotlin.reflect.typeOf

@Composable
fun AppNavHost(modifier: Modifier = Modifier, navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = RecitersScreenRoute,
        modifier = modifier
    ) {
        composable<RecitersScreenRoute> {
            RecitersScreen { riwayatReciter, reciterName ->
                navController.navigate(RiwayatScreenRoute(riwayatReciter, reciterName))
            }
        }

        composable<RiwayatScreenRoute>(
            typeMap = mapOf(
                typeOf<List<RecitersMoshafReading>>() to CustomNavType.riwayatType,
            )
        ) {
            val arguments = it.toRoute<RiwayatScreenRoute>()
            Log.d("Al-qiran", arguments.toString())
        }

    }

}