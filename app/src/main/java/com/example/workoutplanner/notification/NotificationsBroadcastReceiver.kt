package com.example.workoutplanner.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationsBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent!!.action.equals("com.tester.alarmmanager")){
            var b = intent.extras
            val notifyMe = Notifications()
            notifyMe.Notify(context!!, b?.getString("message").toString())
        }
        else if(intent?.action.equals("android.intent.action.BOOT_COMPLETED")){
            val saveData = NotificationsSaveData(context!!)
            saveData.setAlarm()
        }
    }


}