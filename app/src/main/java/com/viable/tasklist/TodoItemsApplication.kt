package com.viable.tasklist

import android.app.Application
import com.viable.tasklist.BuildConfig.USE_MOCKS
import com.viable.tasklist.core.CoreModule
import com.viable.tasklist.di.AppComponent
import com.viable.tasklist.di.DaggerAppComponent

class TodoItemsApplication : Application() {

    val coreModule = CoreModule(USE_MOCKS)

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        /*Instabug.Builder(this, "871ae90a742cf736954652c5f547b3bb")
            .setInvocationEvents(InstabugInvocationEvent.SHAKE)
            .build()*/

        appComponent = DaggerAppComponent.builder()
            .build()

        coreModule.init()
    }

    companion object {
        const val PREFERENCES = "app_preferences"
    }
}
