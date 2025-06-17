package com.rajivranjan.trackify.model

data class Task(
    val title: String,
    val isReminderSet: Boolean = false,
    val hour: Int? = null,
    val minute: Int? = null
)
