package com.viable.tasklist.domain

import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.data.cloud.TodoItemResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

interface TaskToLocalMapper<FromT, ToT> : AbstractMapper<FromT, ToT> {
    override fun map(item: FromT): ToT

    class BasicCloudToLocalMapper(private val importanceMapper: ImportanceMapper) :
        TaskToCloudMapper<TodoItemResponse, TodoItem> {
        override fun map(item: TodoItemResponse) =
            TodoItem(
                item.id,
                item.text,
                importanceMapper.map(item.importance),
                item.isCompleted,
                convertToDateTime(item.creationTime),
                item.deadlineTime?.let { convertToDateTime(it) },
                convertToDateTime(item.modificationTime),
            )

        private fun convertToDateTime(time: Long): LocalDateTime {
            return Instant.ofEpochSecond(time)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    }
}
