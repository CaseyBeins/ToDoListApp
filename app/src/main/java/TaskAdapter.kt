package com.example.modudule3app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val context: Context,
    private val taskList: MutableList<Task>,
    private val dbHelper: TaskDatabaseHelper
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.findViewById(R.id.textViewTaskName)
        val taskDescription: TextView = view.findViewById(R.id.textViewTaskDescription)
        val taskCompleted: CheckBox = view.findViewById(R.id.checkBoxCompleted)
        val buttonDeleteTask: ImageButton = view.findViewById(R.id.buttonDeleteTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskName.text = task.name
        holder.taskDescription.text = task.description
        holder.taskCompleted.isChecked = task.completed

        // Handle task completion toggle
        holder.taskCompleted.setOnCheckedChangeListener { _, isChecked ->
            task.completed = isChecked
            dbHelper.updateTask(task)  // Update task in database
        }

        // Handle task deletion
        holder.buttonDeleteTask.setOnClickListener {
            val taskPosition = holder.adapterPosition
            dbHelper.deleteTask(task.id)
            taskList.removeAt(taskPosition)
            notifyItemRemoved(taskPosition)
        }
    }

    override fun getItemCount(): Int = taskList.size
}