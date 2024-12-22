package com.sourabh.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sourabh.todoapp.data.local.entity.Task
import com.sourabh.todoapp.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _showAddTaskPopup = MutableStateFlow(false)
    val showAddTaskPopup: StateFlow<Boolean> = _showAddTaskPopup

    private val _taskBeingEdited = MutableStateFlow<Task?>(null)
    val taskBeingEdited: StateFlow<Task?> = _taskBeingEdited

    private val _showEditTaskPopup = MutableStateFlow(false)
    val showEditTaskPopup: StateFlow<Boolean> = _showEditTaskPopup

    fun showAddTaskPopup() {
        _showAddTaskPopup.value = true
    }

    fun hideAddTaskPopup() {
        _showAddTaskPopup.value = false
    }

    fun showEditTaskPopup(task: Task) {
        _taskBeingEdited.value = task
        _showEditTaskPopup.value = true
    }

    fun hideEditTaskPopup() {
        _taskBeingEdited.value = null
        _showEditTaskPopup.value = false
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            repository.editTask(task)
            loadLocalTask()
            hideEditTaskPopup()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            loadLocalTask()
        }
    }

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
            _isLoading.value = false
        }
    }

}