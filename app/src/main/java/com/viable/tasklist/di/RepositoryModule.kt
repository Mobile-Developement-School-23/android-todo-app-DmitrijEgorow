package com.viable.tasklist.di

import com.viable.tasklist.data.TodoItemsRepository
import com.viable.tasklist.di.scope.MainActivityScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    @MainActivityScope
    fun bindLocalRepository(repository: TodoItemsRepository.Base): TodoItemsRepository
}
