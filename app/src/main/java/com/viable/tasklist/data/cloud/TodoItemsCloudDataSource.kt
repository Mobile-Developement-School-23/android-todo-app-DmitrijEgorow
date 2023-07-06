package com.viable.tasklist.data.cloud

import com.google.gson.Gson
import java.io.Reader

interface TodoItemsCloudDataSource {

    suspend fun getList(): TodoListResponse
    suspend fun addItem(item: TodoItemContainer, revision: Int): TodoSingleItemResponse
    suspend fun updateItem(item: TodoItemContainer, revision: Int): TodoSingleItemResponse
    suspend fun deleteItem(itemId: String, revision: Int): TodoSingleItemResponse

    abstract class Abstract :
        TodoItemsCloudDataSource

    class DefaultCloudDataSource(private val service: TasksService) :
        TodoItemsCloudDataSource.Abstract() {
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
        gson: Gson,
    ) : TodoItemsCloudDataSource.Abstract()
}

/*
class DefaultCloudDataSource(private val service: TasksService, gson: Gson) :
        TodoItemsCloudDataSource.Abstract() {
        override suspend fun getList(): Flow<NetworkState<List<TodoItemResponse>>> = flow {
            emit(NetworkState.Loading)
            val response = service.getList()
            emit(NetworkState.Success(response.list, response.revision))
        }.catch {
            NetworkState.Failure(it)
        }

        override suspend fun addItem(item: TodoItemContainer, revision: Int): Flow<NetworkState<TodoItemResponse>> = flow {
            emit(NetworkState.Loading)
            val response = service.addTask(revision, item)
            emit(NetworkState.Success(response.element, response.revision))
        }.catch {
            emit(NetworkState.Failure(it))
        }

        override suspend fun updateItem(item: TodoItemContainer): Flow<NetworkState<TodoItemResponse>> = flow {
            emit(NetworkState.Loading)
            val response = service.alterTask(1, item.element.id, item)
            emit(NetworkState.Success(response.element, response.revision))
        }.catch {
            emit(NetworkState.Failure(it))
        }

        override suspend fun deleteItem(item: TodoItemContainer): Flow<NetworkState<TodoItemResponse>> = flow {
            emit(NetworkState.Loading)
            val response = service.deleteTask(1, item.element.id)
            emit(NetworkState.Success(response.element, response.revision))
        }    .catch {
            emit(NetworkState.Failure(it))
        }
    }
 */
