package com.joaobapt.todo.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.InputStream

class UserInfoViewModel : ViewModel() {
    private val repository = UserInfoRepository()
    
    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo = _userInfo.asStateFlow()
    
    fun getInfo() {
        viewModelScope.launch { _userInfo.value = repository.getInfo() }
    }
    
    fun update(user: UserInfo) {
        viewModelScope.launch {
            val newUser = repository.update(user)
            if (newUser != null) _userInfo.value = newUser
        }
    }
    
    fun updateAvatar(imageStream: InputStream) {
        viewModelScope.launch {
            val newUser = repository.updateAvatar(imageStream)
            if (newUser != null) _userInfo.value = newUser
        }
    }
}