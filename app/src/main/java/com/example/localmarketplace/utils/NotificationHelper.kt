package com.example.localmarketplace.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Message
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

object NotificationHelper {

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(context: Context, title: String, message: String){

        val channelId = "listing_channel"

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Listings",
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)

    }
}