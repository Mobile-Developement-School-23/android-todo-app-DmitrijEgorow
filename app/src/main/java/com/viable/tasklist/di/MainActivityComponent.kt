package com.viable.tasklist.di

import android.content.Context
import com.viable.tasklist.data.cache.TodoItemsCacheDataSource
import com.viable.tasklist.di.scope.MainActivityScope
import com.viable.tasklist.presentation.EditFragment
import com.viable.tasklist.presentation.ListFragment
import dagger.BindsInstance
import dagger.Subcomponent

@MainActivityScope
@Subcomponent(modules = [DatabaseModule::class, NetworkModule::class, CacheModule::class, RepositoryModule::class])
interface MainActivityComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MainActivityComponent
    }

    fun inject(fragment: ListFragment)

    fun inject(fragment: EditFragment)

}