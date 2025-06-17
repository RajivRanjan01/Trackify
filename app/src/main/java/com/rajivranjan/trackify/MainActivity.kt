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
import com.rajivranjan.trackify.util.AlarmHelper
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
    var pickedHour by remember { mutableStateOf<Int?>(null) }
    var pickedMinute by remember { mutableStateOf<Int?>(null) }

    val tasksFlow by taskDataStore.tasksFlow.collectAsState(initial = emptyList())
    var tasks by remember { mutableStateOf(tasksFlow) }

    LaunchedEffect(tasksFlow) {
        tasks = tasksFlow
    }

    if (isReminderSet && pickedHour == null && pickedMinute == null) {
        ShowTimePicker(
            onTimeChosen = { h, m ->
                pickedHour = h
                pickedMinute = m
            },
            onDismiss = { isReminderSet = false }
        )
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
                    val newTask = Task(
                        title = newTaskText,
                        isReminderSet = isReminderSet,
                        hour = pickedHour,
                        minute = pickedMinute
                    )

                    val updated = tasks + newTask
                    tasks = updated
                    coroutineScope.launch { taskDataStore.saveTasks(updated) }

                    if (isReminderSet && pickedHour != null && pickedMinute != null) {
                        AlarmHelper.setAlarm(
                            context = context,
                            taskTitle = newTask.title,
                            timeInMillis = AlarmHelper.getTimeInMillis(pickedHour!!, pickedMinute!!),
                            taskId = newTask.title.hashCode()
                        )
                    }

                    newTaskText = ""
                    isReminderSet = false
                    pickedHour = null
                    pickedMinute = null
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Set Reminder")
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = isReminderSet,
                onCheckedChange = {
                    isReminderSet = it
                    if (!it) {
                        pickedHour = null
                        pickedMinute = null
                    }
                }
            )
            if (pickedHour != null && pickedMinute != null) {
                Spacer(Modifier.width(8.dp))
                Text(String.format("%02d:%02d", pickedHour, pickedMinute))
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
                        text = "• ${task.title}",
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        val updated = tasks - task
                        tasks = updated
                        coroutineScope.launch {
                            taskDataStore.saveTasks(updated)
                        }
                        if (task.isReminderSet) {
                            AlarmHelper.cancelAlarm(context, task.title.hashCode())
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)

private fun ShowTimePicker(
    onTimeChosen: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
)
{
    val timePickerState = rememberTimePickerState()
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                onTimeChosen(timePickerState.hour, timePickerState.minute)
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Pick reminder time") },
        text = { TimePicker(state = timePickerState) }
    )
}
