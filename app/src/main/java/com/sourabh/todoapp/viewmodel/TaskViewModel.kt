package com.sourabh.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sourabh.todoapp.data.local.entity.Task
import com.sourabh.todoapp.data.remote.RemoteTodo
import com.sourabh.todoapp.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow(emptyList<Task>())
    val tasks: MutableStateFlow<List<Task>> = _tasks

    private val _remoteTasks = MutableStateFlow(emptyList<Task>())
    val remoteTasks: MutableStateFlow<List<Task>> = _remoteTasks


    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.saveTask(task)
            loadLocalTask()
        }
    }

    fun loadLocalTask() {
        viewModelScope.launch {
            _tasks.value = repository.getAllLocalTasks()
        }
    }

    fun loadRemoteTasks() {
        viewModelScope.launch {
            val remoteTodos = repository.getAllRemoteTasks()

            // Mapping RemoteTodo to local Task
            _remoteTasks.value = remoteTodos.map {
                Task(
                    id = it.id,
                    title = it.title,
                    isCompleted = it.completed,
                )
            }
        }
    }

}