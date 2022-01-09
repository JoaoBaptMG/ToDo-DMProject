package com.joaobapt.todo.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import coil.transform.CircleCropTransformation
import com.joaobapt.todo.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.avatarImageView.load("https://goo.gl/gEgYUd") {
            transformations(CircleCropTransformation())
        }
    }
}