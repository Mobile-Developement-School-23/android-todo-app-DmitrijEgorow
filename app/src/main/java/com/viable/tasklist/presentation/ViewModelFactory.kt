package com.viable.tasklist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viable.tasklist.data.TodoItemsRepository
import com.viable.tasklist.data.prefs.PreferencesRepository
import com.viable.tasklist.domain.TaskRepository
import com.viable.tasklist.presentation.notifications.AlarmScheduler
import com.viable.tasklist.presentation.settings.PreferencesViewModel

abstract class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
     class DefaultTaskViewModelFactory(
         private val repository: TodoItemsRepository,
         private val alarmScheduler: AlarmScheduler?
     ) : ViewModelProvider.Factory {
        override fun<T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ItemViewModel(repository, alarmScheduler) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

     class PreferencesViewModelFactory(private val repository: PreferencesRepository) : ViewModelProvider.Factory {
        override fun<T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PreferencesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PreferencesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
