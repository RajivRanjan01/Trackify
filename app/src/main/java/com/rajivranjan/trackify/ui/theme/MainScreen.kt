package com.rajivranjan.trackify.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourpackage.ui.TaskItem

@Composable
fun MainScreen() {
    var taskText by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = taskText,
                onValueChange = { taskText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter task") }
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

        if (tasks.isEmpty()) {
            Text("No tasks yet", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(tasks) { task ->
                    TaskItem(task = task)
                }
            }
        }
    }
}
