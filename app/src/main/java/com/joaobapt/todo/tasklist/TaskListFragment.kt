package com.joaobapt.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaobapt.todo.databinding.FragmentTaskListBinding
import com.joaobapt.todo.form.FormActivity
import com.joaobapt.todo.network.Api
import com.joaobapt.todo.network.TasksRepository
import com.joaobapt.todo.replace
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    
    // It must be here to be able to be accessed by formLauncher
    private lateinit var taskListAdapter: TaskListAdapter
    
    private val tasksRepository = TasksRepository()
    
    private val formLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null)
            lifecycleScope.launch { tasksRepository.createOrUpdate(task) }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onResume() {
        super.onResume()
        
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            binding.userInfoText.text = "${userInfo.firstName} ${userInfo.lastName}"
        }
        
        lifecycleScope.launch { tasksRepository.refresh() }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Create the list adapter with our own list listener
        taskListAdapter = TaskListAdapter(object : TaskListListener {
            override fun onClickEdit(task: Task) = startFormActivity(task)
            override fun onClickDelete(task: Task) {
                lifecycleScope.launch { tasksRepository.delete(task) }
            }
            override fun onLongClick(task: Task) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${task.title}\n${task.description}")
                }
                
                startActivity(Intent.createChooser(intent, null))
            }
        })
        
        // Initialize the RecyclerView
        with(binding.taskListRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = taskListAdapter
        }
        
        // Bind actions
        binding.taskListFab.setOnClickListener { startFormActivity(null) }
        
        // Collect all the tasks from the server
        lifecycleScope.launch {
            tasksRepository.taskList.collect { taskListAdapter.submitList(it) }
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
}