package com.joaobapt.todo.network

import android.util.Log
import com.joaobapt.todo.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.ExperimentalSerializationApi

class TasksRepository {
    private val webService = Api.tasksWebService
    
    // These two variables hold the same data, one private and one public
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList = _taskList.asStateFlow()
    
    suspend fun refresh(): List<Task>? {
        val response = webService.getTasks()
        Log.e("TasksRepository", "$response")
        return if (response.isSuccessful) response.body() else null
    }
    
    suspend fun create(task: Task): Task? {
        val response = webService.create(task)
        return if (response.isSuccessful) response.body() else null
    }
    
    suspend fun update(task: Task): Task? {
        val response = webService.update(task)
        return if (response.isSuccessful) response.body() else null
    }
    
    suspend fun delete(task: Task): Boolean {
        return webService.delete(task.id).isSuccessful
    }
}