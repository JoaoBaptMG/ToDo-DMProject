package com.joaobapt.todo.tasklist

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class Task(val id: String, val title: String,
                val description: String = "<no description>") : Serializable

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Task, newItem: Task) =
        oldItem.title == newItem.title && oldItem.description == newItem.description
}
