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
import com.alqiran.quraanapp.ui.utils.RequestNotificationPermission
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuraanAppTheme {

                RequestNotificationPermission()

                val backStack = rememberNavBackStack(RecitersScreenRoute)

                val screenWithValue = remember {
                    mutableStateOf("" to "")
                }

                val title: String = when (screenWithValue.value.first) {
                    "reciters_screen" -> screenWithValue.value.second
                    "riwayat_screen" -> screenWithValue.value.second
                    "moshaf_screen" -> screenWithValue.value.second
                    "suwar_screen" -> screenWithValue.value.second
                    else -> ""
                }

                Scaffold(
                    topBar = {
                        TopBar(title = title) {
                            backStack.removeLastOrNull()
                        }
                    }
                ) {
                    AppNavDisplay(
                        modifier = Modifier.padding(it),
                        backStack = backStack,
                        screenWithValue = screenWithValue
                    )
                }
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
