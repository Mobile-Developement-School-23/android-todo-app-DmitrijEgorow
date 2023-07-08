package com.viable.tasklist.di

import com.viable.tasklist.data.TodoItemsRepository
import com.viable.tasklist.data.cloud.TodoItemsCloudDataSource
import com.viable.tasklist.di.scope.ApplicationScope
import com.viable.tasklist.di.scope.MainActivityScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
   /* @Binds
    @ApplicationScope
    fun bindLocalRepository(repository: TodoItemsRepository.Base): TodoItemsRepository*/


    /*@Binds
    @ApplicationScope
    fun bindCloudDataSource(dataSource: TodoItemsCloudDataSource.DefaultCloudDataSource): TodoItemsCloudDataSource*/
}
