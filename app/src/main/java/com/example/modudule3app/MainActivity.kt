package com.example.modudule3app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the activity_main.xml layout file
        setContentView(R.layout.activity_main)

        // Enable edge-to-edge layout
        enableEdgeToEdge()

        // Initialize the database helper
        val dbHelper = TaskDatabaseHelper(this)

        // Fetch all tasks from the database
        val allTasks = dbHelper.getAllTasks().toMutableList()

        // Find the RecyclerView from the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TaskAdapter(this, allTasks, dbHelper)

        // Find the Save Button and add a click listener
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Get task name and description from the input
            val taskName = findViewById<EditText>(R.id.name).text.toString()
            val taskDesc = findViewById<EditText>(R.id.desc).text.toString()

            if (taskName.isNotEmpty()) {
                // Create a new task object
                val newTask = Task(
                    id = 0,
                    name = taskName,
                    description = taskDesc,
                    completed = false
                )



                // Insert the new task into the database
                val id = dbHelper.insertTask(newTask)

                if (id > -1) {
                    Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show()
                    // Refresh RecyclerView with new task
                    newTask.id = id // Update the id after inserting
                    allTasks.add(newTask)
                    recyclerView.adapter?.notifyItemInserted(allTasks.size - 1)
                } else {
                    Toast.makeText(this, "Error adding task", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
