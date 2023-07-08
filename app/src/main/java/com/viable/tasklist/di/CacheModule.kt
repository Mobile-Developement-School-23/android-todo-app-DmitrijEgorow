package com.viable.tasklist.di

import android.provider.Settings
import com.viable.tasklist.data.TodoItemsRepository
import com.viable.tasklist.data.cache.TaskDao
import com.viable.tasklist.data.cache.TodoItemsCacheDataSource
import com.viable.tasklist.data.cloud.TasksService
import com.viable.tasklist.data.cloud.TodoItemsCloudDataSource
import com.viable.tasklist.di.scope.ApplicationScope
import com.viable.tasklist.di.scope.MainActivityScope
import com.viable.tasklist.domain.ImportanceMapper
import com.viable.tasklist.domain.TaskToCloudMapper
import com.viable.tasklist.domain.TaskToLocalMapper
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Named

@Module
class CacheModule {
    @Provides
    @MainActivityScope
    fun provideCacheDataSource(dao: TaskDao) = TodoItemsCacheDataSource(dao)

    @Provides
    @MainActivityScope
    fun provideTodoItemsCloudDataSource(service: TasksService): TodoItemsCloudDataSource =
        TodoItemsCloudDataSource.DefaultCloudDataSource(service)

    @Provides
    @MainActivityScope
    fun provideTodoItemsRepository(
        cacheDataSource: TodoItemsCacheDataSource,
        cloudDataSource: TodoItemsCloudDataSource,
        @Named("DeviceId") deviceId: String
    ): TodoItemsRepository = TodoItemsRepository.Base(
        cacheDataSource,
        cloudDataSource,
        TaskToLocalMapper.BasicCloudToLocalMapper(importanceMapper = ImportanceMapper()),
        TaskToCloudMapper.BasicLocalToCloudMapper(
            importanceMapper = ImportanceMapper(),
            deviceId,
        ),
    )


    @Provides
    @MainActivityScope
    @Named("DeviceId")
    fun provideDeviceId(): String {
        return "13"
    }


}

