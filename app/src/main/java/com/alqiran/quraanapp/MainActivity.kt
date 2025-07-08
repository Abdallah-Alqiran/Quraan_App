package com.alqiran.quraanapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.alqiran.quraanapp.data.Constants.BASE_URL
import com.alqiran.quraanapp.data.RepositoryImpl
import com.alqiran.quraanapp.data.datasources.remote.RemoteDataSource
import com.alqiran.quraanapp.data.datasources.remote.retrofit.api.SuwarApi
import com.alqiran.quraanapp.domain.repository.Repository
import com.alqiran.quraanapp.theme.QuraanAppTheme
import com.alqiran.quraanapp.ui.navigation.AppNavHost
import com.alqiran.quraanapp.ui.screens.reciters_package.RecitersScreen
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuraanAppTheme {

                val rememberScreen = remember {
                    mutableStateOf("")
                }
                val controller = rememberNavController()

                when(rememberScreen.value) {
                    "reciters_screen" -> Log.d("Al-qiran", "First")
                    "riwayat_screen" -> Log.d("Al-qiran", "Second")
                }

                Scaffold (
                    topBar = {
                    }
                ){
                    AppNavHost(modifier = Modifier.padding(it), navController = controller, rememberScreen)
                }


//                val api = Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//                    .create(SuwarApi::class.java)
//                val remoteDataSource = RemoteDataSource(api)
//                val repository: Repository = RepositoryImpl(remoteDataSource)
//                LaunchedEffect(key1 = true) {
//                    Log.d("Al-qiran Suwar", repository.getAllSuwar().toString())
//                    Log.d("Al-qiran riwayat", repository.getAllRiwayat().toString())
//                    Log.d("Al-qiran moshaf", repository.getAllMoshaf().toString())
//                    Log.d("Al-qiran recentReads", repository.getAllRecentReads().toString())
//                    Log.d("Al-qiran radios", repository.getAllRadios().toString())
//                    Log.d("Al-qiran reciters", repository.getAllReciters().toString())
//                }
            }
        }
    }


    override fun attachBaseContext(newBase: Context) {
        val locale = Locale.forLanguageTag("ar")
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }
}
