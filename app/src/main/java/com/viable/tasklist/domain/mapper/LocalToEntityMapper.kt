package com.viable.tasklist.domain.mapper

import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.data.cache.TaskEntity
import java.time.ZoneOffset

interface LocalToEntityMapper<FromT, ToT> : AbstractMapper<FromT, ToT> {
    override fun map(item: FromT): ToT

    class BasicLocalToEntityMapper() :
        LocalToEntityMapper<TodoItem, TaskEntity> {
        override fun map(todoItem: TodoItem) =
            TaskEntity(
                id = todoItem.id,
                text = todoItem.text,
                importance = todoItem.importance,
                isDone = todoItem.isCompleted,
                creationTime = todoItem.createdAt.toEpochSecond(ZoneOffset.UTC),
                deadline = todoItem.deadline?.toEpochSecond(ZoneOffset.UTC),
                modifyingTime = todoItem.updatedAt?.toEpochSecond(ZoneOffset.UTC),
            )
    }
}
