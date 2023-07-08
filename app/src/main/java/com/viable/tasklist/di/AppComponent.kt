package com.viable.tasklist.di

import com.viable.tasklist.TodoItemsApplication
import com.viable.tasklist.data.cache.TodoItemsCacheDataSource
import com.viable.tasklist.di.scope.ApplicationScope
import com.viable.tasklist.di.scope.MainActivityScope
import com.viable.tasklist.presentation.ListFragment
import dagger.Component
import dagger.Provides

@ApplicationScope
@Component
interface AppComponent {
    fun mainActivityComponent(): MainActivityComponent.Factory

    fun inject(application: TodoItemsApplication)

}
