package com.joaobapt.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joaobapt.todo.databinding.ActivityFormBinding
import com.joaobapt.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}