package com.alqiran.quraanapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberNavBackStack
import com.alqiran.quraanapp.theme.QuraanAppTheme
import com.alqiran.quraanapp.ui.components.topbar.TopBar
import com.alqiran.quraanapp.ui.navigation.AppNavDisplay
import com.alqiran.quraanapp.ui.navigation.RecitersScreenRoute
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuraanAppTheme {

                val backStack = rememberNavBackStack(RecitersScreenRoute)


                val screenWithValue = remember {
                    mutableStateOf("" to "")
                }

                val title: String = when(screenWithValue.value.first) {
                    "reciters_screen" -> screenWithValue.value.second
                    "riwayat_screen" -> screenWithValue.value.second
                    "moshaf_screen" -> screenWithValue.value.second
                    else -> ""
                }

                Scaffold (
                    topBar = {
                        TopBar(title = title) {
                            backStack.removeLastOrNull()
                        }
                    }
                ){
                    AppNavDisplay(modifier = Modifier.padding(it), backStack = backStack, screenWithValue =  screenWithValue)
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
