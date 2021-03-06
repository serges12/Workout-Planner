package com.example.workoutplanner.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.util.*

class NotificationsSaveData {
    var context: Context?= null
    var sharedPreferences: SharedPreferences?=null
    constructor(context: Context){
        this.context = context
        sharedPreferences = context.getSharedPreferences("myref", Context.MODE_PRIVATE)
    }
    fun getHour(): Int{
        return sharedPreferences!!.getInt("hour",0)
    }
    fun getMinute(): Int{
        return sharedPreferences!!.getInt("minute",0)
    }
    fun isNotificationOn(): Boolean{
        return sharedPreferences!!.getBoolean("on",true)
    }
    fun SaveData(hour: Int, minute: Int, on:Boolean){
        var editor = sharedPreferences?.edit()
        editor?.putInt("hour", hour)
        editor?.putInt("minute", minute)
        editor?.putBoolean("on",on)
        editor?.commit()
    }

    fun setAlarm(){
        val hour: Int = getHour()
        val minute: Int = getMinute()
        val on: Boolean = isNotificationOn()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND,0)
        val am = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        //If notifications are on in settings
        if(on) {
            var intent = Intent(context, NotificationsBroadcastReceiver::class.java)
            intent.putExtra("message", "Check out what you have for the day! Let's go!")
            intent.action="com.tester.alarmmanager"
            val pi = PendingIntent.getBroadcast(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pi)
        }
    }
}