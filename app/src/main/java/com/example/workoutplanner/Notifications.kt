package com.example.workoutplanner

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat

class Notifications {
    fun Notify(context: Context, message:String){
        val notificationId = 69
        val channelId = "channel1"
        var notificationManager= context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, channelId)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Workout Time!")
                .setContentText(message)
                //add here app icon
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(channelId, "Notification Channel 1", NotificationManager.IMPORTANCE_DEFAULT)
                notificationChannel.description = "This notification channel is used to notify the user."
                notificationChannel.enableVibration(true)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.BLUE
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        val notification : Notification = builder.build()
        if(notificationManager!=null){
            notificationManager.notify(notificationId,notification)
        }
    }
}