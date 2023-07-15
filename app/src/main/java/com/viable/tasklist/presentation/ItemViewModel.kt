package com.viable.tasklist.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viable.tasklist.data.ObtainedData
import com.viable.tasklist.data.RevisionableList
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.data.TodoItemsRepository
import com.viable.tasklist.data.cloud.ReceivedData
import com.viable.tasklist.presentation.notifications.AlarmScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemViewModel(
    private val repository: TodoItemsRepository,
    private val alarmScheduler: AlarmScheduler?,
) : ViewModel() {

    private val _selectedPosition = MutableLiveData(0)
    val selectedPosition: LiveData<Int> get() = _selectedPosition

    private val _selectedTodoItem = MutableLiveData<TodoItem?>(null)
    val selectedTodoItem: LiveData<TodoItem?> get() = _selectedTodoItem

    private val _obtainedData: MutableLiveData<ObtainedData> = MutableLiveData(ObtainedData.Empty(ReceivedData.Empty()))
    val obtainedData: LiveData<ObtainedData> get() = _obtainedData

    private var job: Job? = null
    private var _tasks: MutableStateFlow<TaskUi<List<TodoItem>>> =
        MutableStateFlow(TaskUi.Empty)
    val tasks: StateFlow<TaskUi<List<TodoItem>>>
        get() = _tasks

    init {
        loadItems(true)
    }

    fun loadItems(forceRefresh: Boolean = false) {
        job = viewModelScope.launch(Dispatchers.IO) {
            repository.getItems(forceRefresh)
                .collect { item ->
                    _tasks.value = TaskUi.Success(item.flow)
                    requestNotifications(item)
                }
        }
    }

    fun updateTodoItem(position: Int, todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val obtainedData = repository.updateItem(position, todoItem)
            loadItems(true)
            Log.d("gsonon", obtainedData.toString())
        }
    }

    fun insertNewItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val obtainedData = repository.insertNewItem(
                todoItem,
            )
            Log.d("gsonon_insert", obtainedData.toString())
            withContext(Dispatchers.Main) {
                _obtainedData.value = obtainedData
            }
        }
    }

    fun resetObtainedData() {
        _obtainedData.value = ObtainedData.Empty(ReceivedData.Empty())
    }

    fun deleteTodoItem(todoItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val obtainedData = repository.deleteItem(todoItemId)
            loadItems(true)
            Log.d("gsonon_delete", obtainedData.toString())
        }
    }

    fun setTodoItem(item: TodoItem?) {
        _selectedTodoItem.postValue(item)
    }

    fun setSelectedPosition(position: Int) {
        _selectedPosition.postValue(position)
    }

    fun requestNotifications(items: RevisionableList<List<TodoItem>>) {
        items.flow.forEach { item ->
            alarmScheduler!!.scheduleAlarm(item)
        }
    }
}
