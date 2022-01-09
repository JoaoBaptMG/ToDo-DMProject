package com.joaobapt.todo.network

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.joaobapt.todo.tasklist.TasksWebService
import com.joaobapt.todo.user.UserWebService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object Api {
    private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
    
    lateinit var appContext: Context
    
    fun setupContext(context: Context) {
        appContext = context
    }
    
    private val prefManager get() = PreferenceManager.getDefaultSharedPreferences(appContext)
    
    private fun getToken() = prefManager.getString("auth_token_key", "")
    
    fun setToken(token: String) {
        prefManager.edit { putString("auth_token_key", token)  }
    }
    
    fun eraseToken() {
        prefManager.edit { remove("auth_token_key") }
    }
    
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val token = getToken()
                if (token != null) requestBuilder.addHeader("Authorization", "Bearer $token")
                chain.proceed(requestBuilder.build())
            }.build()
    }
    
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    private val converterFactory =
        jsonSerializer.asConverterFactory("application/json".toMediaType())
    
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