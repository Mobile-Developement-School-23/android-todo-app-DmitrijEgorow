package com.viable.tasklist.domain.mapper

import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.data.cache.TaskEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

interface EntityToLocalMapper<FromT, ToT> : AbstractMapper<FromT, ToT> {
    override fun map(item: FromT): ToT

    class BasicEntityToLocalMapper() :
        EntityToLocalMapper<TaskEntity, TodoItem> {
        override fun map(item: TaskEntity) =
            TodoItem(
                item.id,
                item.text,
                item.importance,
                item.isDone,
                convertToDateTime(item.creationTime),
                item.deadline?.let { convertToDateTime(it) },
                item.modifyingTime?.let { convertToDateTime(it) },
            )
        private fun convertToDateTime(time: Long): LocalDateTime {
            return Instant.ofEpochSecond(time)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    }
}
