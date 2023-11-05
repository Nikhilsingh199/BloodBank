package com.example.jubloodbank

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.app.AlarmManager
import android.app.PendingIntent
import java.util.*

class SupportAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // This method will be called when the alarm is triggered
        // You can implement your logic here
        Toast.makeText(context, "Alarm triggered for Customer Support", Toast.LENGTH_SHORT).show()
        // For example, you can start an activity or show a notification
        // For simplicity, I'm just showing a toast message here
    }
    fun scheduleAlarm(context: Context) {
        // Get the AlarmManager service
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create an intent for the SupportAlarmReceiver class
        val intent = Intent(context, SupportAlarmReceiver::class.java)

        // Create a PendingIntent to be triggered when the alarm goes off
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Set the alarm to start at a specific time (in this example, 9:00 AM every day)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 9) // Set the alarm time (hour)
        calendar.set(Calendar.MINUTE, 0)       // Set the alarm time (minute)

        // Set the alarm to repeat every day
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,            // Alarm type: RTC_WAKEUP wakes up the device when the alarm goes off
            calendar.timeInMillis,              // Alarm time
            AlarmManager.INTERVAL_DAY,          // Repeat interval: 1 day
            pendingIntent                       // PendingIntent to be triggered
        )

        // Display a message indicating that the alarm has been scheduled
        Toast.makeText(context, "Alarm scheduled for Customer Support at 9:00 AM", Toast.LENGTH_SHORT).show()
    }
}
