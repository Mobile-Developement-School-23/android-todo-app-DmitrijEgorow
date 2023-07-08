package com.viable.tasklist.data.cache

import com.viable.tasklist.data.RevisionableList
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.di.scope.MainActivityScope
import com.viable.tasklist.domain.mapper.EntityToLocalMapper
import com.viable.tasklist.domain.mapper.LocalToEntityMapper
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

    private val toDBMapper = LocalToEntityMapper.BasicLocalToEntityMapper()
    private val fromDBMapper = EntityToLocalMapper.BasicEntityToLocalMapper()

    fun isEmpty(): Boolean {
        return _itemsFlow.value.flow.isEmpty() && dao.getTasksAsList().isEmpty()
    }

    suspend fun getItems(): MutableStateFlow<RevisionableList<List<TodoItem>>> {
        if (_itemsFlow.value.flow.isEmpty()) {
            _itemsFlow.tryEmit(RevisionableList(dao.getTasksAsList().map { entity -> fromDBMapper.map(entity) }, revision))
        }
        return _itemsFlow
    }

    suspend fun setItems(items: MutableList<TodoItem>, revision: Int) {
        _itemsFlow.value = RevisionableList(items, revision)
        val taskEntities = items.map(toDBMapper::map)
        dao.alterTasks(taskEntities)
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
        dao.alterTask(toDBMapper.map(item))
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
        dao.alterTask(toDBMapper.map(item))
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
        dao.deleteTaskById(itemId)
        return true
    }

    private fun getItemById(itemId: String): TodoItem {
        return _itemsFlow.value.flow.first { it.id == itemId }
    }
}
