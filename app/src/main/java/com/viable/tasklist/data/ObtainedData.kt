package com.viable.tasklist.data

import com.viable.tasklist.data.cloud.ReceivedData
import com.viable.tasklist.data.cloud.TodoSingleItemResponse
import com.viable.tasklist.domain.TasksReceivedDataMapper

sealed class ObtainedData {

    abstract fun <T> map(mapper: TasksReceivedDataMapper<T>): T

    data class Success(private val item: TodoSingleItemResponse) : ObtainedData() {
        override fun <T> map(mapper: TasksReceivedDataMapper<T>) = mapper.map(item)
    }

    data class Fail(private val e: Exception) : ObtainedData() {
        override fun <T> map(mapper: TasksReceivedDataMapper<T>) = mapper.map(e)
    }

    data class Empty(private val t: ReceivedData.Empty) : ObtainedData() {
        override fun <T> map(mapper: TasksReceivedDataMapper<T>) = mapper.map(t)
    }
}
