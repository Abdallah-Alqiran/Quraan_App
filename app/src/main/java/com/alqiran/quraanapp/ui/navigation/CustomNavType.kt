package com.alqiran.quraanapp.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {

    val riwayatType = object: NavType<List<RecitersMoshafReading>>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): List<RecitersMoshafReading>? {
            return Json.decodeFromString(bundle.getString(key)?: return null)
        }

        override fun parseValue(value: String): List<RecitersMoshafReading> {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: List<RecitersMoshafReading>): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: List<RecitersMoshafReading>) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

}