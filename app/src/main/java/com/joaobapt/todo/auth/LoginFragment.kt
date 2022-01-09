package com.joaobapt.todo.auth

import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.joaobapt.todo.MainActivity
import com.joaobapt.todo.R
import com.joaobapt.todo.databinding.FragmentAuthenticationBinding
import com.joaobapt.todo.databinding.FragmentLoginBinding
import com.joaobapt.todo.network.Api
import com.joaobapt.todo.user.UserWebService
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
    
        with(binding) {
            logInButton.setOnClickListener {
                val email = loginEmail.text.toString()
                val password = loginPassword.text.toString()
                
                if (email.isNotBlank() && password.isNotBlank()) {
                    lifecycleScope.launch {
                        val response = Api.userWebService.login(LoginForm(email, password))
                        val body = response.body()
                        
                        if (response.isSuccessful && body != null) {
                            val token = body.token

                            PreferenceManager.getDefaultSharedPreferences(context).edit {
                                putString("auth_token_key", token)
                            }
                            
                            startActivity(Intent(context, MainActivity::class.java))
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