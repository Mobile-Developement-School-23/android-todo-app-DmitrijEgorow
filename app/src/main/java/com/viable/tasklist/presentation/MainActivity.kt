package com.viable.tasklist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.viable.tasklist.R
import com.viable.tasklist.TodoItemsApplication
import com.viable.tasklist.databinding.ActivityMainBinding
import com.viable.tasklist.di.MainActivityComponent
import com.viable.tasklist.presentation.notifications.AlarmScheduler
import com.viable.tasklist.presentation.notifications.NotificationHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var activityComponent: MainActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent = (applicationContext as TodoItemsApplication)
            .appComponent
            .mainActivityComponent()
            .create(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.createNotificationChannel()

    }
}
