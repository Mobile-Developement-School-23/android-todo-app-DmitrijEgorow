package com.viable.tasklist.data.cache

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTask(id: String): Flow<TaskEntity?>

    @Insert(entity = TaskEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun alterTask(task: TaskEntity)

    @Insert(entity = TaskEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun alterTasks(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: String)

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks")
    fun getTasksAsList(): List<TaskEntity>
}
