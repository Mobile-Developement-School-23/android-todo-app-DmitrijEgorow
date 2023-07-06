package com.viable.tasklist.data.cloud

import com.google.gson.annotations.SerializedName

data class TodoListResponse(
    @SerializedName("list")
    val list: List<TodoItemResponse>,
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("status")
    val status: String,
)
