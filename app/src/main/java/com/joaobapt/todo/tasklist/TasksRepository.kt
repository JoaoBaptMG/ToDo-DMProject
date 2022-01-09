package com.joaobapt.todo.tasklist

import android.util.Log
import com.joaobapt.todo.network.Api

class TasksRepository {
    private val webService = Api.tasksWebService
    
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