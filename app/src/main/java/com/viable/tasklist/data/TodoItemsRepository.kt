package com.viable.tasklist.data

import android.util.Log
import com.viable.tasklist.data.cache.TodoItemsCacheDataSource
import com.viable.tasklist.data.cloud.TodoItemContainer
import com.viable.tasklist.data.cloud.TodoItemResponse
import com.viable.tasklist.data.cloud.TodoItemsCloudDataSource
import com.viable.tasklist.data.cloud.TodoSingleItemResponse
import com.viable.tasklist.di.scope.MainActivityScope
import com.viable.tasklist.domain.TaskRepository
import com.viable.tasklist.domain.mapper.AbstractMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import retrofit2.HttpException
import javax.inject.Inject

interface TodoItemsRepository : TaskRepository {
    suspend fun getItems(forceRefresh: Boolean = false): Flow<RevisionableList<List<TodoItem>>>
    suspend fun insertNewItem(item: TodoItem): ObtainedData
    suspend fun updateItem(position: Int, item: TodoItem): ObtainedData
    suspend fun deleteItem(itemId: String): ObtainedData

    @MainActivityScope
    class Base @Inject constructor(
        private val cachedDataSource: TodoItemsCacheDataSource,
        private val cloudDataSource: TodoItemsCloudDataSource,
        private val receiverMapper: AbstractMapper<TodoItemResponse, TodoItem>,
        private val senderMapper: AbstractMapper<TodoItem, TodoItemResponse>,
    ) : TodoItemsRepository {

        private var revision = 1
        override suspend fun getItems(forceRefresh: Boolean): Flow<RevisionableList<List<TodoItem>>> {
            return if (cachedDataSource.isEmpty() || forceRefresh) {
                try {
                    val list = getCloudResponse()
                    val convertedList = ArrayList<TodoItem>()
                    list.forEach { e -> convertedList.add(receiverMapper.map(e)) }
                    cachedDataSource.setItems(convertedList, revision)
                    cachedDataSource.getItems()
                } catch (e: HttpException) {
                    emptyFlow()
                } catch (e: Exception) {
                    emptyFlow()
                }
            } else {
                cachedDataSource.getItems()
            }
        }

        private suspend fun getCloudResponse(): List<TodoItemResponse> {
            val response = cloudDataSource.getList()
            val revision = response.revision
            Log.d("gsonon", "revision $revision")
            this.revision = revision
            return response.list
        }

        override suspend fun insertNewItem(item: TodoItem): ObtainedData {
            return try {
                cachedDataSource.addItem(item)
                getCloudResponse()
                val container = TodoItemContainer(senderMapper.map(item))
                val singleItemResponse = cloudDataSource.addItem(container, revision)
                returnSuccess(singleItemResponse)
            } catch (e: HttpException) {
                returnFail(e)
            } catch (e: Exception) {
                returnFail(e)
            }
        }

        override suspend fun updateItem(position: Int, item: TodoItem): ObtainedData {
            return try {
                val updateList = cachedDataSource.updateItem(position, item)
                getCloudResponse()
                val container = TodoItemContainer(senderMapper.map(item))
                val singleItemResponse = cloudDataSource.updateItem(container, revision)
                returnSuccess(singleItemResponse)
            } catch (e: HttpException) {
                returnFail(e)
            } catch (e: Exception) {
                returnFail(e)
            }
        }
        override suspend fun deleteItem(itemId: String): ObtainedData {
            return try {
                cachedDataSource.deleteItems(itemId)
                getCloudResponse()
                val singleItemResponse = cloudDataSource.deleteItem(itemId, revision)
                returnSuccess(singleItemResponse)
            } catch (e: HttpException) {
                returnFail(e)
            } catch (e: Exception) {
                returnFail(e)
            }
        }
        protected fun returnSuccess(v: TodoSingleItemResponse) = ObtainedData.Success(v)
        protected fun returnFail(e: Exception) = ObtainedData.Fail(e)
    }
}
