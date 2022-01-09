package com.joaobapt.todo.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaobapt.todo.R
import com.joaobapt.todo.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }
}