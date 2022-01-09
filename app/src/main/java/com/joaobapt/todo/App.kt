package com.joaobapt.todo

import android.app.Application
import com.joaobapt.todo.network.Api

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Api.setupContext(this)
    }
}