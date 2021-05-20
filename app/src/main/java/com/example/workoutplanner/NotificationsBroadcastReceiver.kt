package com.example.workoutplanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationsBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent!!.action.equals("com.tester.alarmmanager")){
            var b = intent.extras
//            Toast.makeText(context, b?.getString("message"), Toast.LENGTH_LONG).show()
            val notifyMe = Notifications()
            notifyMe.Notify(context!!, b?.getString("message").toString(),10)
        }
        else if(intent?.action.equals("android.intent.action.BOOT_COMPLETED")){
            val saveData = NotificationsSaveData(context!!)
            saveData.setAlarm()
        }
    }


}