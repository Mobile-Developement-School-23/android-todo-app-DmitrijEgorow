package com.viable.tasklist.data

import kotlinx.coroutines.flow.MutableStateFlow

interface TodoItemsRepository {
    fun getItems(): MutableStateFlow<List<TodoItem>>
    fun insertNewItem(item: TodoItem)
    fun updateItem(position: Int, item: TodoItem)

    class Base(
        private val dataSource: TodoItemsCacheDataSource,
    ) : TodoItemsRepository {
        override fun getItems() = dataSource.getItems()

        override fun insertNewItem(item: TodoItem) {
            dataSource.addItem(item)
        }

        override fun updateItem(position: Int, item: TodoItem) {
            dataSource.updateItem(position, item)
        }
    }
}
