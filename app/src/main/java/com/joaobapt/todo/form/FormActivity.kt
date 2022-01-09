package com.joaobapt.todo.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joaobapt.todo.R
import com.joaobapt.todo.databinding.ActivityFormBinding
import com.joaobapt.todo.tasklist.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get the task ID (or null)
        val oldTask = intent.getSerializableExtra("task") as? Task
        if (oldTask != null) {
            binding.taskEditTitle.setText(oldTask.title)
            binding.taskEditDescription.setText(oldTask.description)
        }
        
        val taskId = oldTask?.id ?: UUID.randomUUID().toString()
        
        binding.confirmButton.setOnClickListener {
            val newTask = Task(id = taskId,
                title = binding.taskEditTitle.text.toString(),
                description = binding.taskEditDescription.text.toString())
            
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}