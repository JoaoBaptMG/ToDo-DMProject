package com.joaobapt.todo.user

import android.net.Uri
import com.joaobapt.todo.network.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream

class UserInfoRepository {
    private val webService = Api.userWebService
    
    suspend fun getInfo(): UserInfo? {
        val response = webService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }
    
    suspend fun update(user: UserInfo): UserInfo? {
        val response = webService.update(user)
        return if (response.isSuccessful) response.body() else null
    }
    
    suspend fun updateAvatar(imageStream: InputStream): UserInfo? {
        val part = MultipartBody.Part.createFormData(
            name = "avatar", filename = "temp.jpeg",
            body = imageStream.readBytes().toRequestBody()
        )
        
        val response = webService.updateAvatar(part)
        return if (response.isSuccessful) response.body() else null
    }
}