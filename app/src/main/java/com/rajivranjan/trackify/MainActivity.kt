package com.rajivranjan.trackify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    var taskText by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Trackify",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = taskText,
                onValueChange = { taskText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter a task") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                if (taskText.isNotBlank()) {
                    tasks.add(taskText)
                    taskText = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Text(
                    text = "• $task",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
