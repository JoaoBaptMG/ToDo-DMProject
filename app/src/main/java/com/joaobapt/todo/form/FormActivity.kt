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
        
        binding.confirmButton.setOnClickListener {
            val newTask = Task(id = UUID.randomUUID().toString(),
                title = binding.taskEditTitle.text.toString(),
                description = binding.taskEditDescription.text.toString())
            
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}