package com.example.modudule3app

import android.app.DownloadManager.COLUMN_DESCRIPTION
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_TASKS = "tasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"  // Add this line
        private const val COLUMN_COMPLETED = "completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("Database", "Creating table with columns: $COLUMN_ID, $COLUMN_NAME, $COLUMN_DESCRIPTION, $COLUMN_COMPLETED")
        val createTable = "CREATE TABLE $TABLE_TASKS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +  // Include this line in table creation
                "$COLUMN_COMPLETED INTEGER)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("Database", "Upgrading database from version $oldVersion to $newVersion")
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_TASKS ADD COLUMN $COLUMN_DESCRIPTION TEXT")  // Include the new column if upgrading
        }
    }




    fun insertTask(task: Task): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, task.name)
            put(COLUMN_DESCRIPTION, task.description)  // Insert description
            put(COLUMN_COMPLETED, if (task.completed) 1 else 0)
        }
        return db.insert(TABLE_TASKS, null, values)
    }

    fun updateTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, task.name)
            put(COLUMN_DESCRIPTION, task.description)  // Update description
            put(COLUMN_COMPLETED, if (task.completed) 1 else 0)
        }
        db.update(TABLE_TASKS, values, "$COLUMN_ID = ?", arrayOf(task.id.toString()))
    }

    fun deleteTask(taskId: Long) {
        val db = writableDatabase
        db.delete(TABLE_TASKS, "$COLUMN_ID = ?", arrayOf(taskId.toString()))
    }

    fun getAllTasks(): MutableList<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val cursor = db.query(TABLE_TASKS, null, null, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                    ?: ""  // Safely handle null values
                val completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1

                // Use the retrieved values to create a Task object
                val task = Task(
                    id = id,
                    name = name,
                    description = description,
                    completed = completed
                )

                taskList.add(task)
            } while (cursor.moveToNext())
            cursor.close()
        }

        return taskList
    }
}