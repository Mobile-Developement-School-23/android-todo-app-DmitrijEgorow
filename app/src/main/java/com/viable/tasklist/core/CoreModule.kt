package com.viable.tasklist.core

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CoreModule(private val useMocks: Boolean) {

    private companion object {
        const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
    }

    private lateinit var retrofit: Retrofit

    fun init(/*context: Context*/) {
        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            )
            .addInterceptor(AuthenticationInterceptor())
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> makeService(clazz: Class<T>): T = retrofit.create(clazz)

    // override fun viewModel() = MainViewModel(navigator, navigationCommunication)
}
