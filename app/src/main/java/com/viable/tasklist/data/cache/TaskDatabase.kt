package com.viable.tasklist.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class, RevisionEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
