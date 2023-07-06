package com.viable.tasklist.di

import com.viable.tasklist.data.cloud.TasksService
import com.viable.tasklist.di.scope.MainActivityScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class NetworkModule {
    @Provides
    @MainActivityScope
    fun provideLoginRetrofitService(): TasksService {
        return Retrofit.Builder()
            .baseUrl("https://example.com")
            .build()
            .create(TasksService::class.java)
    }
}
