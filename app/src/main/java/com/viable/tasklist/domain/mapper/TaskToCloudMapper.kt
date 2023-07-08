package com.viable.tasklist.domain.mapper

import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.data.cloud.TodoItemResponse
import java.time.ZoneOffset

interface TaskToCloudMapper<FromT, ToT> : AbstractMapper<FromT, ToT> {
    override fun map(item: FromT): ToT

    class BasicLocalToCloudMapper(private val importanceMapper: ImportanceMapper, private val deviceId: String) :
        TaskToCloudMapper<TodoItem, TodoItemResponse> {
        override fun map(item: TodoItem) =
            TodoItemResponse(
                item.id,
                item.text,
                importanceMapper.map(item.importance),
                item.isCompleted,
                item.createdAt.toEpochSecond(ZoneOffset.UTC),
                item.deadline?.toEpochSecond(ZoneOffset.UTC),
                item.createdAt.toEpochSecond(ZoneOffset.UTC),
                null,
                deviceId,
            )
    }
}
