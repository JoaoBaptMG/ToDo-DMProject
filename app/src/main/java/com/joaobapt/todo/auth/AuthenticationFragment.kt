package com.joaobapt.todo.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.joaobapt.todo.R
import com.joaobapt.todo.databinding.FragmentAuthenticationBinding

class AuthenticationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        
        val binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        binding.authLogInButton.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
        }
        binding.authSignUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_signupFragment)
        }
        return binding.root
    }
}