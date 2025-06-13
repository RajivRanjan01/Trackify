package com.rajivranjan.trackify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rajivranjan.trackify.ui.theme.TrackifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackifyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TaskListScreen()
                }
            }
        }
    }
}

@Composable
fun TaskListScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Trackify",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        // This is where we'll render your real task list UI soon
        Text(text = "No tasks yet! Let’s get productive.")
    }
}
