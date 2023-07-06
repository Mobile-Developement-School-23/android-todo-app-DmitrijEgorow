package com.viable.tasklist.presentation

sealed class TaskUi<out T> {
    object Empty : TaskUi<Nothing>()
    object Progress : TaskUi<Nothing>()
    data class Success<T>(val data: T, val message: String? = null) : TaskUi<T>()
    data class Error(val cause: String) : TaskUi<Nothing>()
}
