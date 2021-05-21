package com.example.workoutplanner

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class NotificationsBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent!!.action.equals("com.tester.alarmmanager")){
            var b = intent.extras
//            Toast.makeText(context, b?.getString("message"), Toast.LENGTH_LONG).show()
            val notifyMe = Notifications()
            notifyMe.Notify(context!!, b?.getString("message").toString())
        }
        else if(intent?.action.equals("android.intent.action.BOOT_COMPLETED")){
            val saveData = NotificationsSaveData(context!!)
            saveData.setAlarm()
        }
    }


}