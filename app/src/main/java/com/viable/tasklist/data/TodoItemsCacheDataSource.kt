package com.viable.tasklist.data

import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import java.util.UUID

class TodoItemsCacheDataSource {

    private val todoItems = mutableListOf(
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Купить молоко",
            importance = Importance.NORMAL,
            isCompleted = true,
            createdAt = LocalDateTime.now(),
        ),
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Забрать посылку",
            importance = Importance.URGENT,
            isCompleted = false,
            createdAt = LocalDateTime.now(),
        ),
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Позвонить другу",
            importance = Importance.LOW,
            isCompleted = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now().plusHours(1),
        ),
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Заплатить за квартиру",
            importance = Importance.NORMAL,
            deadline = LocalDateTime.now().plusDays(5),
            isCompleted = false,
            createdAt = LocalDateTime.now(),
        ),
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Подготовиться к собеседованию",
            importance = Importance.URGENT,
            deadline = LocalDateTime.now().plusDays(3),
            isCompleted = false,
            createdAt = LocalDateTime.now(),
        ),
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Купить топленое молоко, сметану, творог, ряженку, кефир, снежок, " +
                "айран, йогурт, сливки, масло, сыр и что-то ещё",
            importance = Importance.NORMAL,
            deadline = LocalDateTime.now().plusDays(1),
            isCompleted = false,
            createdAt = LocalDateTime.now(),
        ),
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Забрать посылку",
            importance = Importance.URGENT,
            isCompleted = false,
            createdAt = LocalDateTime.now(),
        ),
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Позвонить другу",
            importance = Importance.LOW,
            isCompleted = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now().plusDays(1),
        ),
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Заплатить за квартиру",
            importance = Importance.NORMAL,
            deadline = LocalDateTime.now().plusDays(5),
            isCompleted = false,
            createdAt = LocalDateTime.now(),
        ),
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Подготовиться к собеседованию",
            importance = Importance.URGENT,
            deadline = LocalDateTime.now().plusDays(3),
            isCompleted = false,
            createdAt = LocalDateTime.now(),
        ),
        TodoItem(
            id = UUID.randomUUID().toString(),
            text = "Напечатать постер",
            importance = Importance.URGENT,
            deadline = LocalDateTime.now().plusDays(3),
            isCompleted = true,
            createdAt = LocalDateTime.now(),
        ),
    )
    private val _itemsFlow = MutableStateFlow<List<TodoItem>>(todoItems)

    fun getItems(): MutableStateFlow<List<TodoItem>> {
        return _itemsFlow
    }

    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    fun updateItem(position: Int, item: TodoItem) {
        todoItems[position] = item
    }
}
