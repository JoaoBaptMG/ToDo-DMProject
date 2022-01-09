package com.joaobapt.todo.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaobapt.todo.R
import com.joaobapt.todo.databinding.FragmentAuthenticationBinding

class AuthenticationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }
}