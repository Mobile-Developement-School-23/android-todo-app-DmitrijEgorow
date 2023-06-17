package com.viable.tasklist

import android.app.Application
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent
import com.viable.tasklist.data.TodoItemsCacheDataSource
import com.viable.tasklist.data.TodoItemsRepository

class TodoItemsApplication : Application() {

    lateinit var dataSource: TodoItemsCacheDataSource
    lateinit var repository: TodoItemsRepository

    override fun onCreate() {
        super.onCreate()

        Instabug.Builder(this, "871ae90a742cf736954652c5f547b3bb")
            .setInvocationEvents(InstabugInvocationEvent.SHAKE)
            .build()

        dataSource = TodoItemsCacheDataSource()
        repository = TodoItemsRepository.Base(dataSource)
    }
}
