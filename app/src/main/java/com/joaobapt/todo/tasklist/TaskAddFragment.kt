package com.joaobapt.todo.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.joaobapt.todo.databinding.FragmentAddTaskBinding
import com.joaobapt.todo.getNavigationParam
import com.joaobapt.todo.removeNavigationResult
import com.joaobapt.todo.setNavigationResult

class TaskAddFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        val oldTask = getNavigationParam<Task>("paramTask")
        if (oldTask != null) {
            binding.taskEditTitle.setText(oldTask.title)
            binding.taskEditDescription.setText(oldTask.description)
            removeNavigationResult<Task>("paramTask")
        }
        
        val taskId = oldTask?.id ?: ""
        
        binding.confirmButton.setOnClickListener {
            val newTask = Task(id = taskId,
                               title = binding.taskEditTitle.text.toString(),
                               description = binding.taskEditDescription.text.toString())
            
            setNavigationResult(newTask, "newTask")
            findNavController().popBackStack()
        }
        
        return binding.root
    }
}