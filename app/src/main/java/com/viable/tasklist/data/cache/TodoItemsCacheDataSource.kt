package com.viable.tasklist.data.cache

import com.viable.tasklist.data.RevisionableList
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.di.scope.ApplicationScope
import com.viable.tasklist.di.scope.MainActivityScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@MainActivityScope
class TodoItemsCacheDataSource @Inject constructor(
    private val dao: TaskDao,
) {

    private var revision = 1
    private val _itemsFlow = MutableStateFlow<RevisionableList<List<TodoItem>>>(
        RevisionableList(mutableListOf(), revision),
    )

    fun isEmpty(): Boolean {
        return _itemsFlow.value.flow.isEmpty()
    }

    fun getItems(): MutableStateFlow<RevisionableList<List<TodoItem>>> {
        return _itemsFlow
    }

    fun setItems(item: MutableList<TodoItem>, revision: Int) {
        _itemsFlow.value = RevisionableList(item, revision)
    }

    suspend fun addItem(item: TodoItem) {
        val tempList = _itemsFlow.value

        this._itemsFlow.emit(
            RevisionableList(
                tempList.flow.toMutableList().also { v ->
                    v.add(item)
                },
                tempList.revision,
            ),
        )
    }

    suspend fun updateItem(position: Int, item: TodoItem): StateFlow<RevisionableList<List<TodoItem>>> {
        val tempList = _itemsFlow.value
        this._itemsFlow.emit(
            RevisionableList(
                tempList.flow.toMutableList().also { v ->
                    v[position] = item
                },
                tempList.revision,
            ),
        )
        return _itemsFlow
    }

    suspend fun deleteItems(itemId: String): Boolean {
        val tempList = _itemsFlow.value
        this._itemsFlow.emit(
            RevisionableList(
                tempList.flow.toMutableList().apply {
                    remove(getItemById(itemId))
                },
                tempList.revision,
            ),
        )
        return true
    }

    suspend fun getItemById(itemId: String): TodoItem {
        return _itemsFlow.value.flow.first { it.id == itemId }
    }
}
