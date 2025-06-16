package com.rajivranjan.trackify

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajivranjan.trackify.model.Task
import com.rajivranjan.trackify.ui.theme.TrackifyTheme
import com.rajivranjan.trackify.util.TaskDataStore
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackifyTheme {
                TaskListScreen(context = applicationContext)
            }
        }
    }
}

@Composable
fun TaskListScreen(context: Context) {
    val taskDataStore = remember { TaskDataStore(context) }
    val coroutineScope = rememberCoroutineScope()

    var newTaskText by remember { mutableStateOf("") }
    var isReminderSet by remember { mutableStateOf(false) }

    val tasksFlow by taskDataStore.tasksFlow.collectAsState(initial = emptyList())
    var tasks by remember { mutableStateOf(tasksFlow) }

    LaunchedEffect(tasksFlow) {
        tasks = tasksFlow
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Trackify Tasks", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newTaskText,
                onValueChange = { newTaskText = it },
                label = { Text("Enter task") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newTaskText.isNotBlank()) {
                    val newTask = Task(name = newTaskText, reminderSet = isReminderSet)
                    val updatedTasks = tasks + newTask
                    tasks = updatedTasks
                    coroutineScope.launch {
                        taskDataStore.saveTasks(updatedTasks)
                    }
                    newTaskText = ""
                    isReminderSet = false
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Set Reminder")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isReminderSet,
                onCheckedChange = { isReminderSet = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "• ${task.name}",
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        val updatedTasks = tasks - task
                        tasks = updatedTasks
                        coroutineScope.launch {
                            taskDataStore.saveTasks(updatedTasks)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            }
        }
    }
}
