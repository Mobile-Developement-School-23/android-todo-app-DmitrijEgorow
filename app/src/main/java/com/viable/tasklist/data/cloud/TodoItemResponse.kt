package com.viable.tasklist.data.cloud

import com.google.gson.annotations.SerializedName

data class TodoItemResponse(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("done") val isCompleted: Boolean,
    @SerializedName("created_at") val creationTime: Long,
    @SerializedName("deadline") val deadlineTime: Long?,
    @SerializedName("changed_at") val modificationTime: Long,

    @SerializedName("color") val color: String?,
    @SerializedName("last_updated_by") val lastUpdatedBy: String,

)
