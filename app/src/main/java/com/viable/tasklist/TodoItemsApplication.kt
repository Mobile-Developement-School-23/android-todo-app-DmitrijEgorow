package com.viable.tasklist

import android.app.Application
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent
import com.viable.tasklist.BuildConfig.USE_MOCKS
import com.viable.tasklist.core.CoreModule
import com.viable.tasklist.di.AppComponent
import com.viable.tasklist.di.DaggerAppComponent

class TodoItemsApplication : Application() {

    val coreModule = CoreModule(USE_MOCKS)

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .build()

        coreModule.init()
    }
}
