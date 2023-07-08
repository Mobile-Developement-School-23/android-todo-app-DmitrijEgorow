package com.viable.tasklist.di

import com.viable.tasklist.BuildConfig.USE_MOCKS
import com.viable.tasklist.core.CoreModule
import com.viable.tasklist.data.cloud.TasksService
import com.viable.tasklist.di.scope.MainActivityScope
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {
    @Provides
    @MainActivityScope
    fun provideLoginRetrofitService(): TasksService {
        val module = CoreModule(USE_MOCKS)
        module.init()
        return module.makeService(TasksService::class.java)

    }
    /*return Retrofit.Builder()
        .baseUrl("https://example.com")
        .build()
        .create(TasksService::class.java)*/

}
