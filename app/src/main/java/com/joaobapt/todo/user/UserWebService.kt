package com.joaobapt.todo.user

import com.joaobapt.todo.auth.AuthResponse
import com.joaobapt.todo.auth.LoginForm
import com.joaobapt.todo.auth.SignupForm
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserWebService {
    @POST("users/sign_up")
    suspend fun signup(@Body user: SignupForm): Response<AuthResponse>
    
    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<AuthResponse>
    
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>
    
    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>
    
    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>
}
