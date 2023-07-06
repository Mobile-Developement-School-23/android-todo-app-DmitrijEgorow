package com.viable.tasklist.data

import com.viable.tasklist.data.cloud.TodoSingleItemResponse
import com.viable.tasklist.domain.AbstractMapper
import com.viable.tasklist.domain.TasksReceivedDataMapper


sealed class ObtainedData {

    abstract fun <T> map(mapper: TasksReceivedDataMapper<T>): T

    data class Success(private val item: TodoSingleItemResponse) : ObtainedData() {
        override fun <T> map(mapper: TasksReceivedDataMapper<T>) = mapper.map(item)
    }

    data class Fail(private val e: Exception) : ObtainedData() {
        override fun <T> map(mapper: TasksReceivedDataMapper<T>) = mapper.map(e)
    }
}