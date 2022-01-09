package com.joaobapt.todo.tasklist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaobapt.todo.databinding.ActivityFormBinding
import java.util.*

class TaskAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        var taskId: String? = null
        
        // Try to share from another app
        if (intent?.action == Intent.ACTION_SEND) {
            val string = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (string != null)
                binding.taskEditDescription.setText(string)
        }
        else {
            // Get the task ID (or null)
            val oldTask = intent?.getSerializableExtra("task") as? Task
            if (oldTask != null) {
                binding.taskEditTitle.setText(oldTask.title)
                binding.taskEditDescription.setText(oldTask.description)
                taskId = oldTask.id
            }
        }
        
        taskId = taskId ?: UUID.randomUUID().toString()
        
        binding.confirmButton.setOnClickListener {
            val newTask = Task(id = taskId,
                               title = binding.taskEditTitle.text.toString(),
                               description = binding.taskEditDescription.text.toString())
            
            intent?.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}