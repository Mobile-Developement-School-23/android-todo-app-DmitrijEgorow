package com.viable.tasklist.data.cloud

import com.google.gson.Gson
import com.viable.tasklist.di.scope.ApplicationScope
import java.io.Reader
import javax.inject.Inject

interface TodoItemsCloudDataSource {

    suspend fun getList(): TodoListResponse
    suspend fun addItem(item: TodoItemContainer, revision: Int): TodoSingleItemResponse
    suspend fun updateItem(item: TodoItemContainer, revision: Int): TodoSingleItemResponse
    suspend fun deleteItem(itemId: String, revision: Int): TodoSingleItemResponse

    class DefaultCloudDataSource @Inject constructor(private val service: TasksService) :
        TodoItemsCloudDataSource {
        override suspend fun getList(): TodoListResponse {
            return service.getList()
        }

        override suspend fun addItem(item: TodoItemContainer, revision: Int): TodoSingleItemResponse {
            return service.addTask(revision, item)
        }

        override suspend fun updateItem(item: TodoItemContainer, revision: Int): TodoSingleItemResponse {
            return service.alterTask(revision, item.element.id, item)
        }

        override suspend fun deleteItem(itemId: String, revision: Int): TodoSingleItemResponse {
            return service.deleteTask(revision, itemId)
        }
    }

    abstract class Mock(
        private val rawResourceReader: Reader,
    ) : TodoItemsCloudDataSource
}

