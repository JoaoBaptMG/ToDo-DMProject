package com.joaobapt.todo.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@ExperimentalSerializationApi
object Api {
    private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
    // Not recommended, it should be somewhere else where it isn't pushed to Git,
    // but it's a learning project, so whatever (I'm sure I'll receive an email
    // from a GitHub bot warning a token is leaking)
    private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo2NzQsImV4cCI6MTY3MzI0NzA5Mn0.igIOfhIbV3G3PMXooDVpNjJkKgfXMa3El1bVfjt1wow"
    
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN").build()
                chain.proceed(newRequest)
            }.build()
    }
    
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    private val converterFactory = jsonSerializer.asConverterFactory("application/json".toMediaType())
    
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
    
    val userWebService: UserWebService by lazy { retrofit.create(UserWebService::class.java) }
    val tasksWebService: TasksWebService by lazy { retrofit.create(TasksWebService::class.java) }
}