package com.viable.tasklist

import android.app.Application
import android.provider.Settings
import com.viable.tasklist.BuildConfig.USE_MOCKS
import com.viable.tasklist.core.CoreModule
import com.viable.tasklist.data.TodoItemsRepository
import com.viable.tasklist.data.cache.TodoItemsCacheDataSource
import com.viable.tasklist.data.cloud.TasksService
import com.viable.tasklist.data.cloud.TodoItemsCloudDataSource
import com.viable.tasklist.domain.ImportanceMapper
import com.viable.tasklist.domain.TaskToCloudMapper
import com.viable.tasklist.domain.TaskToLocalMapper

class TodoItemsApplication : Application() {

    val coreModule = CoreModule(USE_MOCKS)

    lateinit var cacheDataSource: TodoItemsCacheDataSource
    lateinit var cloudDataSource: TodoItemsCloudDataSource
    lateinit var repository: TodoItemsRepository

    override fun onCreate() {
        super.onCreate()

        /*Instabug.Builder(this, "871ae90a742cf736954652c5f547b3bb")
            .setInvocationEvents(InstabugInvocationEvent.SHAKE)
            .build()*/

        coreModule.init(this)

        cacheDataSource = TodoItemsCacheDataSource()
        cloudDataSource = TodoItemsCloudDataSource.DefaultCloudDataSource(
            coreModule.makeService(TasksService::class.java),
            coreModule.gson,

        )
        repository = TodoItemsRepository.Base(
            cacheDataSource,
            cloudDataSource,
            TaskToLocalMapper.BasicCloudToLocalMapper(importanceMapper = ImportanceMapper()),
            TaskToCloudMapper.BasicLocalToCloudMapper(
                importanceMapper = ImportanceMapper(),
                getDeviceId(),
            ),
        )
    }

    private fun getDeviceId() =
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}
