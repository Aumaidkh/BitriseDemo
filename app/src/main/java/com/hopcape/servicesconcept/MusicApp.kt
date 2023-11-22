package com.hopcape.servicesconcept

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log

private const val TAG = "MusicApp"
class MusicApp: Application() {

    companion object {
        const val CHANNEL_ID = "newmusicChannel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Notifications",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = "This is the channel description"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}