package com.rajivranjan.trackify

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajivranjan.trackify.ui.theme.TrackifyTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.rajivranjan.trackify.util.TaskDataStore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete



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

    val tasksFlow = taskDataStore.tasksFlow.collectAsState(initial = emptySet())
    var newTask by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(tasksFlow.value.toList()) }

    LaunchedEffect(tasksFlow.value) {
        tasks = tasksFlow.value.toList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Trackify Tasks", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        Row {
            TextField(
                value = newTask,
                onValueChange = { newTask = it },
                label = { Text("Enter task") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newTask.isNotBlank()) {
                        val updatedTasks = tasks + newTask
                        tasks = updatedTasks
                        coroutineScope.launch {
                            taskDataStore.saveTasks(updatedTasks.toSet())
                        }
                        newTask = ""
                    }
                }
            ) {
                Text("Add")
            }
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
                        text = "• $task",
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        val updatedTasks = tasks - task
                        tasks = updatedTasks
                        coroutineScope.launch {
                            taskDataStore.saveTasks(updatedTasks.toSet())
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

