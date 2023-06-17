package com.viable.tasklist.data

import java.time.LocalDateTime

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    var isCompleted: Boolean,
    val createdAt: LocalDateTime,
    val deadline: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
)

enum class Importance {
    LOW,
    NORMAL,
    URGENT,
}
