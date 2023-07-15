package com.viable.tasklist.di

import android.content.Context
import com.viable.tasklist.di.scope.MainActivityScope
import com.viable.tasklist.presentation.MainActivity
import com.viable.tasklist.presentation.edit.EditFragment
import com.viable.tasklist.presentation.list.ListFragment
import com.viable.tasklist.presentation.settings.PreferencesFragment
import dagger.BindsInstance
import dagger.Subcomponent

@MainActivityScope
@Subcomponent(modules = [DatabaseModule::class, NetworkModule::class, RepositoryModule::class])
interface MainActivityComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MainActivityComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: ListFragment)
    fun inject(fragment: EditFragment)
    fun inject(fragment: PreferencesFragment)
}
