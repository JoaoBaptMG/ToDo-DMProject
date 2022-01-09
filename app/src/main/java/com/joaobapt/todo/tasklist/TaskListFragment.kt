package com.joaobapt.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaobapt.todo.databinding.FragmentTaskListBinding
import com.joaobapt.todo.form.FormActivity
import java.util.*

class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var taskListAdapter: TaskListAdapter
    
    val formLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null) {
            taskList = taskList + task
            taskListAdapter.submitList(taskList)
        }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        taskListAdapter = TaskListAdapter()
        
        binding.taskListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskListAdapter
        }
        taskListAdapter.submitList(taskList)
        
        binding.taskListFab.setOnClickListener {
            formLauncher.launch(Intent(context, FormActivity::class.java))
        }
        
        taskListAdapter.onClickDelete = { task ->
            taskList = taskList - task
            taskListAdapter.submitList(taskList)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private var taskList = listOf<Task>()
}