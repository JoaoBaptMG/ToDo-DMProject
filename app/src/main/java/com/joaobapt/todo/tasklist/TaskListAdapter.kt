package com.joaobapt.todo.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joaobapt.todo.databinding.ItemTaskBinding

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {
    var onClickEdit: (Task) -> Unit = {}
    var onClickDelete: (Task) -> Unit = {}
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
    
    inner class TaskViewHolder(private val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(task: Task) {
            with(binding) {
                taskTitle.text = task.title
                taskDescription.text = task.description
                taskEditButton.setOnClickListener { onClickEdit(task) }
                taskRemoveButton.setOnClickListener { onClickDelete(task) }
            }
        }
    }
}