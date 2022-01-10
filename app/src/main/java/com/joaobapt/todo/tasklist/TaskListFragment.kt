package com.joaobapt.todo.tasklist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.joaobapt.todo.R
import com.joaobapt.todo.databinding.FragmentTaskListBinding
import com.joaobapt.todo.getNavigationResult
import com.joaobapt.todo.getNavigationResultLiveData
import com.joaobapt.todo.network.Api
import com.joaobapt.todo.setNavigationParam
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    
    // It must be here to be able to be accessed by formLauncher
    private val viewModel: TaskListViewModel by viewModels()
    
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
            
            if (!response.isSuccessful) navigateToAuthFragment()
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
        
        binding.userAvatar.setOnClickListener { navigateToUserInfoFragment() }
        
        val newTask = getNavigationResult<Task>("newTask")
        if (newTask != null) viewModel.addOrEdit(newTask)
    }
    
    private fun startFormActivity(task: Task?) {
        if (task != null) setNavigationParam(task, "paramTask")
        findNavController().navigate(R.id.action_taskListFragment_to_taskAddFragment)
    }
    
    private fun navigateToAuthFragment() {
        findNavController().clearBackStack(R.id.action_taskListFragment_to_authenticationFragment)
    }
    
    private fun navigateToUserInfoFragment() {
        findNavController().navigate(R.id.action_taskListFragment_to_userInfoFragment)
    }
    
    private fun startShareActivity(task: Task) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "${task.title}\n${task.description}")
        }
        
        startActivity(Intent.createChooser(intent, null))
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}