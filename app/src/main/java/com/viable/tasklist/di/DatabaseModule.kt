package com.viable.tasklist.di

import android.content.Context
import androidx.room.Room
import com.viable.tasklist.data.cache.TaskDao
import com.viable.tasklist.data.cache.TaskDatabase
import com.viable.tasklist.di.scope.ApplicationScope
import com.viable.tasklist.di.scope.MainActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @MainActivityScope
    fun provideDatabase(context: Context): TaskDatabase = Room
        .databaseBuilder(
            context,
            TaskDatabase::class.java,
            "task_database",
        ).build()

    @Provides
    @MainActivityScope
    fun provideTaskDao(database: TaskDatabase): TaskDao = database.taskDao()
}
