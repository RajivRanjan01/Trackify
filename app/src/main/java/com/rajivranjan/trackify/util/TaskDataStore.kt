package com.rajivranjan.trackify.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rajivranjan.trackify.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("tasks_prefs")

class TaskDataStore(private val context: Context) {

    private val TASKS_KEY = stringPreferencesKey("tasks_key")
    private val gson = Gson()

    val tasksFlow: Flow<List<Task>> = context.dataStore.data.map { preferences ->
        val json = preferences[TASKS_KEY] ?: "[]"
        val type = object : TypeToken<List<Task>>() {}.type
        gson.fromJson(json, type) ?: emptyList()
    }

    suspend fun saveTasks(tasks: List<Task>) {
        val json = gson.toJson(tasks)
        context.dataStore.edit { preferences ->
            preferences[TASKS_KEY] = json
        }
    }
}
