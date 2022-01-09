package com.joaobapt.todo.tasklist

interface TaskListListener {
    fun onClickEdit(task: Task)
    fun onClickDelete(task: Task)
    fun onLongClick(task: Task)
}

