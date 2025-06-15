package com.rajivranjan.trackify.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "task_prefs")

class TaskDataStore(private val context: Context) {
    private val TASKS_KEY = stringSetPreferencesKey("tasks_key")

    val tasksFlow: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[TASKS_KEY] ?: emptySet()
        }

    suspend fun saveTasks(tasks: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[TASKS_KEY] = tasks
        }
    }
}
