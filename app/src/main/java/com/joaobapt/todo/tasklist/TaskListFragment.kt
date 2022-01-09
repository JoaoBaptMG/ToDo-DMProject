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

// Support function
fun <E> Iterable<E>.replace(old: E, new: E) = map { if (it == old) new else it }

class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    
    // It must be here to be able to be accessed by formLauncher
    private lateinit var taskListAdapter: TaskListAdapter
    
    private val formLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null) {
            // Replace the old task if necessary, if not, add it to the end of the list
            val oldTask = taskList.firstOrNull { it.id == task.id }
            taskList = if (oldTask != null)
                taskList.replace(oldTask, task)
            else taskList + task
            
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
        
        // Initialize the RecyclerView
        with(binding.taskListRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = taskListAdapter
        }
        
        // Bind actions
        binding.taskListFab.setOnClickListener { startFormActivity(null) }
        
        with (taskListAdapter) {
            onClickEdit = { startFormActivity(it) }
            onClickDelete = { task ->
                taskList = taskList - task
                submitList(taskList)
            }
        }
    }
    
    private fun startFormActivity(task: Task?) {
        val intent = Intent(context, FormActivity::class.java)
        if (task != null) intent.putExtra("task", task)
        formLauncher.launch(intent)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private var taskList = listOf<Task>()
}