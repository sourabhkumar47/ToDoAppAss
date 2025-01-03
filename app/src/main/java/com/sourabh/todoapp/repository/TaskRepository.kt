package com.sourabh.todoapp.repository

import com.sourabh.todoapp.data.local.dao.TaskDao
import com.sourabh.todoapp.data.local.entity.Task
import com.sourabh.todoapp.data.remote.ApiService
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val apiService: ApiService
) {
    //local operations
    suspend fun saveTask(task: Task) = taskDao.insertTask(task)

    suspend fun getAllLocalTasks() = taskDao.getTasks()

    //remote operations
    suspend fun getAllRemoteTasks() = apiService.getTodos()
}