package com.viable.tasklist.data.cloud

import okhttp3.ResponseBody
import retrofit2.http.*

interface TasksService {

    @GET("list")
    suspend fun getList(): TodoListResponse

    @GET("list/{id}")
    suspend fun getTask(@Path(value = "id") id: String): ResponseBody

    @POST("list")
    suspend fun addTask(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body task: TodoItemContainer
    ): TodoSingleItemResponse

    @PUT("list/{id}")
    suspend fun alterTask(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path(value = "id") id: String,
        @Body task: TodoItemContainer
    ): TodoSingleItemResponse

    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body listRequest: TodoListContainer
    ): TodoListResponse

    @DELETE("list/{id}")
    suspend fun deleteTask(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path(value = "id") id: String
    ): TodoSingleItemResponse
}

