package com.viable.tasklist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.data.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {

    private val _selectedPosition = MutableLiveData(0)
    val selectedPosition: LiveData<Int> get() = _selectedPosition

    private val _selectedTodoItem = MutableLiveData<TodoItem?>(null)
    val selectedTodoItem: LiveData<TodoItem?> get() = _selectedTodoItem

    private var _items = MutableStateFlow<List<TodoItem>>(emptyList())
    val items: StateFlow<List<TodoItem>>
        get() = _items

    fun loadItems(repository: TodoItemsRepository) {
        viewModelScope.launch {
            _items = repository.getItems()
        }
    }

    fun setTodoItem(item: TodoItem?) {
        _selectedTodoItem.postValue(item)
    }

    fun setSelectedPosition(position: Int) {
        _selectedPosition.postValue(position)
    }
}
