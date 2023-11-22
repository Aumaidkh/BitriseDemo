package com.hopcape.servicesconcept

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

private const val TAG = "MusicService"

class MusicService: Service() {

    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioTracks : MutableList<Int>
    private var currentTrack = 0

    private fun loadAudioFiles(){
        Log.d(TAG, "loadAudioFiles: ")
        audioTracks = mutableListOf(
            R.raw.first,
            R.raw.second
        )
    }

    private fun instantiateMediaPlayer(){
        Log.d(TAG, "instantiateMediaPlayer: ")
        mediaPlayer = MediaPlayer.create(this,audioTracks.first())
        mediaPlayer?.apply {
            start()
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Music Service Created...")
        loadAudioFiles()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let { 
            when(it?.action){
                ACTION_START -> {
                    startService()
                }
                ACTION_STOP -> {
                    stopService()
                }
                ACTION_NEXT -> {
                    handleNext()
                }
                ACTION_PREVIOUS -> {
                    handlePrevious()
                }
                else -> {}
            }
        }
        return START_STICKY
    }
    
    private fun startService(){
        Log.d(TAG, "startService: ")
        instantiateMediaPlayer()
        startForeground(1,createNotification())

    }
    
    private fun stopService(){
        Log.d(TAG, "stopService: ")
        mediaPlayer?.apply {
            release()
        }
        mediaPlayer = null
        stopForeground(STOP_FOREGROUND_LEGACY)
    }
    
    private fun handleNext(){
        Log.d(TAG, "handleNext: ")
        if (currentTrack < audioTracks.size - 1){
            currentTrack++
            mediaPlayer?.apply {
                release()
            }
            mediaPlayer = MediaPlayer.create(this,audioTracks[currentTrack])
            mediaPlayer?.apply {
                start()
            }

            createNotification().also {
                notificationManager.notify(1,it)
            }

        }
    }
    
    private fun handlePrevious(){
        if (currentTrack > 0){
            currentTrack--
            mediaPlayer?.apply {
                release()
            }
            mediaPlayer = MediaPlayer.create(this,audioTracks[currentTrack])
            mediaPlayer?.apply {
                start()
            }
            createNotification().also {
                notificationManager.notify(1,it)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        if (mediaPlayer != null){
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun createNotification(): Notification {

        // Get the layouts to use in the custom notification.
        val notificationLayout = RemoteViews(packageName, R.layout.collapsed_notification)
        val notificationLayoutExpanded = RemoteViews(packageName, R.layout.expanded_notification)

        // Create intents for each action
        val playIntent = Intent(this,MusicService::class.java).apply {
            action = ACTION_START
        }
        val nextIntent = Intent(this,MusicService::class.java).apply {
            action = ACTION_NEXT
        }
        val previousIntent = Intent(this,MusicService::class.java).apply {
            action = ACTION_PREVIOUS
        }

        // Create PendingIntents for each action
        val playPendingIntent = PendingIntent.getService(applicationContext, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)
        val nextPendingIntent = PendingIntent.getService(applicationContext, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE)
        val previousPendingIntent = PendingIntent.getService(applicationContext, 0, previousIntent, PendingIntent.FLAG_IMMUTABLE)

        // Set the PendingIntents to corresponding buttons in the RemoteViews

        notificationLayoutExpanded.setOnClickPendingIntent(R.id.btnPlay, playPendingIntent)
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.btnNext, nextPendingIntent)
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.btnPrev, previousPendingIntent)




        val builder =  NotificationCompat.Builder(this)
            .setContentTitle("Now Playing")
            .setContentText("Track $currentTrack")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpanded)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setChannelId(MusicApp.CHANNEL_ID)
        }

        return builder.build()
    }
    
    
    companion object {
        const val ACTION_START = "start"
        const val ACTION_STOP = "stop"
        const val ACTION_NEXT = "next"
        const val ACTION_PREVIOUS = "previous"
    }
}