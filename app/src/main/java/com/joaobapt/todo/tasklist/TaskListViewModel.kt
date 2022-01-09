package com.joaobapt.todo.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaobapt.todo.network.TasksRepository
import com.joaobapt.todo.replace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val repository = TasksRepository()
    
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList = _taskList.asStateFlow()
    
    fun refresh() {
        viewModelScope.launch { _taskList.value = repository.refresh() ?: emptyList() }
    }
    
    fun addOrEdit(task: Task) {
        val oldTask = taskList.value.firstOrNull { it.id == task.id }
        
        // Send the correct request
        viewModelScope.launch {
            val newTask = if (oldTask != null)
                repository.update(task)
            else repository.create(task)
            
            if (newTask != null) {
                _taskList.value = if (oldTask != null)
                    taskList.value.replace(oldTask, newTask)
                else taskList.value + newTask
            }
        }
    }
    
    fun delete(task: Task) {
        if (taskList.value.contains(task)) {
            viewModelScope.launch {
                if (repository.delete(task))
                    _taskList.value = taskList.value - task
            }
        }
    }
}