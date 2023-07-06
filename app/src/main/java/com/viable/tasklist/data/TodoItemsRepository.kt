package com.viable.tasklist.data

import android.util.Log
import com.viable.tasklist.data.cache.TodoItemsCacheDataSource
import com.viable.tasklist.data.cloud.TodoItemContainer
import com.viable.tasklist.data.cloud.TodoItemResponse
import com.viable.tasklist.data.cloud.TodoItemsCloudDataSource
import com.viable.tasklist.data.cloud.TodoSingleItemResponse
import com.viable.tasklist.domain.AbstractMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import retrofit2.HttpException

interface TodoItemsRepository {
    suspend fun getItems(forceRefresh: Boolean = false): Flow<RevisionableList<List<TodoItem>>>
    suspend fun insertNewItem(item: TodoItem): ObtainedData
    suspend fun updateItem(position: Int, item: TodoItem): ObtainedData
    suspend fun deleteItem(itemId: String): ObtainedData

    class Base(
        private val cachedDataSource: TodoItemsCacheDataSource,
        private val cloudDataSource: TodoItemsCloudDataSource,
        private val receiverMapper: AbstractMapper<TodoItemResponse, TodoItem>,
        private val senderMapper: AbstractMapper<TodoItem, TodoItemResponse>,
    ) : TodoItemsRepository {

        private var revision = 1
        override suspend fun getItems(forceRefresh: Boolean): Flow<RevisionableList<List<TodoItem>>> {
            if (cachedDataSource.isEmpty() || forceRefresh) {
                try {
                    val response = cloudDataSource.getList()
                    val revision = response.revision
                    Log.d("gsonon", "revision $revision")
                    this.revision = revision
                    val list = response.list
                    val convertedList = ArrayList<TodoItem>()
                    list.forEach { e -> convertedList.add(receiverMapper.map(e)) }
                    cachedDataSource.setItems(convertedList, revision)
                    return cachedDataSource.getItems()
                } catch (e: HttpException) {
                    return emptyFlow()
                }
            } else {
                return cachedDataSource.getItems()
            }
        }

        override suspend fun insertNewItem(item: TodoItem): ObtainedData {
            try {
                cachedDataSource.addItem(item)
                /*val revisionableData = getItems(true)
                var revision = 1
                revisionableData.collectLatest { v -> revision = v.revision }*/
                val container = TodoItemContainer(senderMapper.map(item))
                val singleItemResponse = cloudDataSource.addItem(container, revision)
                return returnSuccess(singleItemResponse)
            } catch (e: HttpException) {
                return returnFail(e)
            }
        }

        override suspend fun updateItem(position: Int, item: TodoItem): ObtainedData {
            return try {
                val updateList = cachedDataSource.updateItem(position, item)
                //  write code to obtain item using itemId
                // val item = cachedDataSource.getItemById(item)
                val container = TodoItemContainer(senderMapper.map(item))
                val singleItemResponse = cloudDataSource.updateItem(container, revision)
                returnSuccess(singleItemResponse)
            } catch (e: HttpException) {
                returnFail(e)
            }
        }
        override suspend fun deleteItem(itemId: String): ObtainedData {
            return try {
                // todo cachedDataSource.delete
                val singleItemResponse = cloudDataSource.deleteItem(itemId, revision)
                returnSuccess(singleItemResponse)
            } catch (e: HttpException) {
                returnFail(e)
            }
        }
        protected fun returnSuccess(v: TodoSingleItemResponse) = ObtainedData.Success(v)
        protected fun returnFail(e: Exception) = ObtainedData.Fail(e)
    }
}
