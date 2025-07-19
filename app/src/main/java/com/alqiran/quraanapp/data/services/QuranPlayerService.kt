package com.alqiran.quraanapp.data.services

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.compose.ui.graphics.Path
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.alqiran.quraanapp.R
import com.alqiran.quraanapp.ui.utils.NotificationConstants.CHANNEL_ID
import com.alqiran.quraanapp.ui.utils.NotificationConstants.NEXT
import com.alqiran.quraanapp.ui.utils.NotificationConstants.PLAY_PAUSE
import com.alqiran.quraanapp.ui.utils.NotificationConstants.PREV
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class QuranPlayerService() : Service() {
    // TODO you should initialize them
    lateinit var  surahUrl: String
    lateinit var surahName: String
    lateinit var reciterName: String

    inner class QuranBinder() : Binder() {
        fun getService() = this@QuranPlayerService

        fun setSurah(/** TODO 18*/) {

        }

        fun currentDuration() = this@QuranPlayerService.currentDuration
        fun maxDuration() = this@QuranPlayerService.maxDuration

        fun isPlaying() = this@QuranPlayerService.isPlaying

        fun getCurrentSurah() = this@QuranPlayerService.currentSurah
    }
    val binder = QuranBinder()

    private var mediaPlayer = MediaPlayer()
    private val currentSurah = MutableStateFlow(surahUrl)

    private val maxDuration = MutableStateFlow(0f)
    private val currentDuration = MutableStateFlow(0f)

    private val scope = CoroutineScope(Dispatchers.Main)
    private var job: Job ?= null

    private val isPlaying = MutableStateFlow(false)

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (intent.action) {
                PREV -> {
                    prev()
                }

                NEXT -> {
                    next()
                }

                PLAY_PAUSE -> {
                    playPause()
                }

                else -> {

                }
            }
        }

        return START_STICKY
    }

    fun updateDuration() {
        job = scope.launch {
            if (mediaPlayer.isPlaying.not()) return@launch
            maxDuration.update { mediaPlayer.duration.toFloat() }
            while (true) {
                currentDuration.update { mediaPlayer.currentPosition.toFloat() }
                delay(1000)
            }

        }
    }

    fun play() {
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(this@QuranPlayerService, surahUrl.toUri())
            prepareAsync()
            setOnPreparedListener {
                mediaPlayer.start()
                sendNotification(surahUrl, surahName, reciterName)
                updateDuration()
            }
        }
    }

    fun playPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.release()
        }
        sendNotification(currentSurah.value, surahName, reciterName)
    }

    fun prev() {
        job?.cancel()
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer()

        // TODO 14:
    }

    fun next() {
        job?.cancel()
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer()

//        TODO 14: val index =
    }

    private fun sendNotification(surahUrl: String, surahName: String, reciterName: String) {

        isPlaying.update { mediaPlayer.isPlaying }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(surahName)
            .setContentText(reciterName)
            .addAction(R.drawable.ic_skip_previous, PREV, createPrevPendingIntent())
            .addAction(R.drawable.ic_skip_next, NEXT, createNextPendingIntent())
            .addAction(
                if (mediaPlayer.isPlaying) R.drawable.ic_play else R.drawable.ic_pause,
                PLAY_PAUSE,
                createPlayPausePendingIntent()
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_launcher_background
                )
            )
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startForeground(1, notification)
            }
        } else {
            startForeground(1, notification)
        }
    }

    fun createPrevPendingIntent(): PendingIntent {
        val intent = Intent(this, QuranPlayerService::class.java).apply {
            action = PREV
        }
        return PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun createNextPendingIntent(): PendingIntent {
        val intent = Intent(this, QuranPlayerService::class.java).apply {
            action = NEXT
        }
        return PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun createPlayPausePendingIntent(): PendingIntent {
        val intent = Intent(this, QuranPlayerService::class.java).apply {
            action = PLAY_PAUSE
        }
        return PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}
