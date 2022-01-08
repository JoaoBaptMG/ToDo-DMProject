package com.joaobapt.todo.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joaobapt.todo.R
import org.w3c.dom.Text

class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>()
{
    var currentList: List<Task> = listOf()
    
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(taskTitle: String, taskDescription: String)
        {
            itemView.findViewById<TextView>(R.id.task_title).text = taskTitle
            itemView.findViewById<TextView>(R.id.task_description).text = taskDescription
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder
    {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }
    
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int)
    {
        currentList[position].apply { holder.bind(title, description) }
    }
    
    override fun getItemCount(): Int = currentList.size
}