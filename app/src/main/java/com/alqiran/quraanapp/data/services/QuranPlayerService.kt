package com.alqiran.quraanapp.data.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder


class QuranBinder: Binder() {

}

class QuranPlayerService: Service() {

    val binder = QuranBinder()


    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }


//    private fun sendNotification(track: Track)

}