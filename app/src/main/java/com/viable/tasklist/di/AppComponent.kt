package com.viable.tasklist.di

import com.viable.tasklist.TodoItemsApplication
import com.viable.tasklist.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component
interface AppComponent {
    fun mainActivityComponent(): MainActivityComponent.Factory

    fun inject(application: TodoItemsApplication)
}
