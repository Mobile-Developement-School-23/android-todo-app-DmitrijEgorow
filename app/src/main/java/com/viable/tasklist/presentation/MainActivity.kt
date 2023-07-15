package com.viable.tasklist.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.viable.tasklist.R
import com.viable.tasklist.TodoItemsApplication
import com.viable.tasklist.data.prefs.PreferencesRepository
import com.viable.tasklist.databinding.ActivityMainBinding
import com.viable.tasklist.di.MainActivityComponent
import com.viable.tasklist.presentation.notifications.NotificationHelper
import com.viable.tasklist.presentation.settings.PreferencesViewModel
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var activityComponent: MainActivityComponent
    @Inject
    lateinit var repositoryPrefs: PreferencesRepository
    val preferencesViewModel: PreferencesViewModel by viewModels<PreferencesViewModel> {
        TaskViewModelFactory.PreferencesViewModelFactory(
        repositoryPrefs,
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent = (applicationContext as TodoItemsApplication)
            .appComponent
            .mainActivityComponent()
            .create(applicationContext)

        activityComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesViewModel.themeLiveData.observe(this) {}
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.createNotificationChannel()

    }
}
