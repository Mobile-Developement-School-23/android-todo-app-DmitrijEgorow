package com.viable.tasklist.di

import com.viable.tasklist.data.cache.TodoItemsCacheDataSource
import com.viable.tasklist.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class CacheModule {
    @Provides
    @ApplicationScope
    fun provideCacheDataSource(cacheDataSource: TodoItemsCacheDataSource) = TodoItemsCacheDataSource()
}