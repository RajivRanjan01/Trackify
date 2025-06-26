package com.rajivranjan.trackify.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val taskTitle = intent?.getStringExtra("TASK_TITLE") ?: "Your Task"

        Log.d("AlarmReceiver", "Alarm received for: $taskTitle")

        Toast.makeText(context, "Reminder: $taskTitle", Toast.LENGTH_LONG).show()
    }
}
