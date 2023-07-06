package com.viable.tasklist.data.cloud

import com.google.gson.annotations.SerializedName

data class TodoSingleItemResponse (
    @SerializedName("element")
    val element: TodoItemResponse,
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("status")
    val status: String
) : ReceivedData



