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
    
    private var taskList = listOf<Task>()
    
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
        super.onCreateView(inflater, container, savedInstanceState)
        
        // Create and retain the instance
        val serializedList = savedInstanceState?.getSerializable("task_list")
        taskList = (serializedList as? ArrayList<*>)?.filterIsInstance<Task>() ?: listOf()
        
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("task_list", ArrayList(taskList))
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Create the list adapter with our own list listener
        taskListAdapter = TaskListAdapter(object : TaskListListener {
            override fun onClickEdit(task: Task) = startFormActivity(task)
            override fun onClickDelete(task: Task) {
                taskList = taskList - task
                taskListAdapter.submitList(taskList)
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
        
        taskListAdapter.submitList(taskList)
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