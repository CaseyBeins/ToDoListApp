package com.example.modudule3app

data class Task(
    var id: Long,
    val name: String,
    val description: String, // for task description field
    var completed: Boolean
)
