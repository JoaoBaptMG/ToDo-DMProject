package com.joaobapt.todo.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.joaobapt.todo.R
import java.util.*

class TaskListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.task_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        val adapter = TaskListAdapter()
        recyclerView.adapter = adapter
        adapter.submitList(taskList)
        
        val fab = view.findViewById<FloatingActionButton>(R.id.task_list_fab)
        fab.setOnClickListener {
            taskList = taskList + Task(id = UUID.randomUUID().toString(),
                                       title = "Task ${taskList.size + 1}")
            adapter.submitList(taskList)
        }
    }
    
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
}