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
import com.joaobapt.todo.databinding.FragmentTaskListBinding
import java.util.*

class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val taskListAdapter = TaskListAdapter()
        binding.taskListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskListAdapter
        }
        taskListAdapter.submitList(taskList)
    
        binding.taskListFab.setOnClickListener {
            taskList = taskList + Task(id = UUID.randomUUID().toString(),
                                       title = "Task ${taskList.size + 1}")
            taskListAdapter.submitList(taskList)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
}