package com.viable.tasklist.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.data.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ItemViewModel(
    private val repository: TodoItemsRepository
) : ViewModel() {

    private val _selectedPosition = MutableLiveData(0)
    val selectedPosition: LiveData<Int> get() = _selectedPosition

    private val _selectedTodoItem = MutableLiveData<TodoItem?>(null)
    val selectedTodoItem: LiveData<TodoItem?> get() = _selectedTodoItem
    private var job: Job? = null
    private var _tasks: MutableStateFlow<TaskUi<List<TodoItem>>> =
        MutableStateFlow(TaskUi.Empty)
    val tasks: StateFlow<TaskUi<List<TodoItem>>>
        get() = _tasks

    init {
        loadItems()
    }

    fun loadItems(forceRefresh: Boolean = false) {
        job = viewModelScope.launch(Dispatchers.IO) {
            repository.getItems(forceRefresh).collect { item -> _tasks.value = TaskUi.Success(item.flow) }
        }
    }

    fun updateTodoItem(position: Int, todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val obtainedData = repository.updateItem(position, todoItem)
            Log.d("gsonon", obtainedData.toString())
        }
    }

    fun insertNewItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val obtainedData = repository.insertNewItem(
                todoItem,
            )
            Log.d("gsonon", obtainedData.toString())
        }

    }

    fun setTodoItem(item: TodoItem?) {
        _selectedTodoItem.postValue(item)
    }

    fun setSelectedPosition(position: Int) {
        _selectedPosition.postValue(position)
    }


}

