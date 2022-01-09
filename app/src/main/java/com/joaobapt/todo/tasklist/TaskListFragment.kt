package com.joaobapt.todo.tasklist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.joaobapt.todo.R
import com.joaobapt.todo.auth.AuthenticationActivity
import com.joaobapt.todo.databinding.FragmentTaskListBinding
import com.joaobapt.todo.network.Api
import com.joaobapt.todo.user.UserInfoActivity
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    
    // It must be here to be able to be accessed by formLauncher
    private val viewModel: TaskListViewModel by viewModels()
    
    private val formLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null) viewModel.addOrEdit(task)
    }
    
    private val userInfoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        val logout = result.data?.getBooleanExtra("logout", false) ?: false
        if (logout) startAuthActivity()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val taskListAdapter = TaskListAdapter(object : TaskListListener {
            override fun onClickEdit(task: Task) = startFormActivity(task)
            override fun onClickDelete(task: Task) = viewModel.delete(task)
            override fun onLongClick(task: Task) = startShareActivity(task)
        })
        
        // Initialize the RecyclerView
        with(binding.taskListRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = taskListAdapter
        }
        
        binding.taskListFab.setOnClickListener { startFormActivity(null) }
        
        lifecycleScope.launch {
            viewModel.taskList.collect { taskListAdapter.submitList(it) }
        }
    }
    
    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        
        lifecycleScope.launch {
            val response = Api.userWebService.refreshToken()
            
            if (!response.isSuccessful) startAuthActivity()
            else {
                val userInfo = Api.userWebService.getInfo().body()
    
                if (userInfo != null) {
                    binding.userInfoText.text = "${userInfo.firstName} ${userInfo.lastName}"
                    binding.userAvatar.load(userInfo.avatar) {
                        error(R.drawable.ic_launcher_background)
                        transformations(CircleCropTransformation())
                    }
                }
    
                viewModel.refresh()
            }
        }
        
        binding.userAvatar.setOnClickListener {
            userInfoLauncher.launch(Intent(context, UserInfoActivity::class.java))
        }
    }
    
    private fun startFormActivity(task: Task?) {
        val intent = Intent(context, TaskAddActivity::class.java)
        if (task != null) intent.putExtra("task", task)
        formLauncher.launch(intent)
    }
    
    private fun startShareActivity(task: Task) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "${task.title}\n${task.description}")
        }
        
        startActivity(Intent.createChooser(intent, null))
    }
    
    private fun startAuthActivity() {
        startActivity(Intent(context, AuthenticationActivity::class.java))
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}