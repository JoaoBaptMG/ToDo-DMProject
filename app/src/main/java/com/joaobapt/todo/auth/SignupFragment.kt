package com.joaobapt.todo.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.joaobapt.todo.MainActivity
import com.joaobapt.todo.R
import com.joaobapt.todo.databinding.FragmentSignupBinding
import com.joaobapt.todo.network.Api
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentSignupBinding.inflate(inflater, container, false)
        
        with(binding) {
            signUpButton.setOnClickListener {
                val firstName = signUpFirstName.text.toString()
                val lastName = signUpLastName.text.toString()
                val email = signUpEmail.text.toString()
                val password = signUpPassword.text.toString()
                val passwordConfirmation = signUpConfirmPassword.text.toString()
                
                val allNonNull = listOf(firstName, lastName, email,
                                        password, passwordConfirmation)
                    .all { it.isNotBlank() }
                
                if (allNonNull && password == passwordConfirmation) {
                    lifecycleScope.launch {
                        val signup = SignupForm(firstName, lastName, email,
                                                password, passwordConfirmation)
                        val response = Api.userWebService.signup(signup)
                        val body = response.body()
                        if (response.isSuccessful && body != null && body.token != null) {
                            Api.setToken(body.token)
                            activity?.finish()
                        }
                        else {
                            val errorMsg = getString(R.string.connection_error)
                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        
        return binding.root
    }
}