package com.joaobapt.todo.user

import retrofit2.Response
import retrofit2.http.GET

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>
}
