package com.rajivranjan.trackify.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val task = intent.getStringExtra("TASK_NAME")
        Toast.makeText(context, "Reminder: $task", Toast.LENGTH_LONG).show()
    }
}
