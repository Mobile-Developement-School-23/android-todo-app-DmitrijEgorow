package com.viable.tasklist.data

data class RevisionableList<T>(
    var flow: T,
    val revision: Int
)