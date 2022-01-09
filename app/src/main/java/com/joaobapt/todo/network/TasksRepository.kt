package com.joaobapt.todo.network

import com.joaobapt.todo.replace
import com.joaobapt.todo.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TasksRepository {
    private val tasksWebService = Api.tasksWebService
    
    // These two variables hold the same data, one private and one public
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList = _taskList.asStateFlow()
    
    suspend fun refresh() {
        // Suspend fun because it's a long operation
        val tasksResponse = tasksWebService.getTasks()
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            if (fetchedTasks != null) _taskList.value = fetchedTasks
        }
    }
    
    suspend fun createOrUpdate(task: Task) {
        val oldTask = taskList.value.firstOrNull { it.id == task.id }
        
        // Send the correct request
        val response = if (oldTask != null)
            tasksWebService.update(task)
        else tasksWebService.create(task)
        
        if (response.isSuccessful) {
            val updatedTask = response.body()!!
            
            _taskList.value = if (oldTask != null)
                taskList.value.replace(oldTask, updatedTask)
            else taskList.value + updatedTask
        }
    }
    
    suspend fun delete(task: Task) {
        // Return if there's no task like this
        if (taskList.value.contains(task)) {
            val response = tasksWebService.delete(task.id)
            if (response.isSuccessful)
                _taskList.value = taskList.value - task
        }
    }
}