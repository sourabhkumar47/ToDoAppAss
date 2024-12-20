package com.sourabh.todoapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sourabh.todoapp.data.local.dao.TaskDao
import com.sourabh.todoapp.data.local.entity.Task

@Database(entities = [Task::class], version = 0, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}