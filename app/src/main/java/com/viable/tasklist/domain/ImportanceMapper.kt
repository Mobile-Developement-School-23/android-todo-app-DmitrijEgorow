package com.viable.tasklist.domain

import com.viable.tasklist.data.Importance


class ImportanceMapper: AbstractMapper<Importance, String> {
    override fun map(importance: Importance): String {
        return when(importance){
            Importance.LOW -> "low"
            Importance.NORMAL -> "basic"
            Importance.URGENT -> "important"
            else -> throw IllegalArgumentException()
        }
    }

    fun map(importance: String): Importance {
        return when(importance){
            "low" -> Importance.LOW
             "basic" -> Importance.NORMAL
             "important" -> Importance.URGENT
            else -> throw IllegalArgumentException()
        }
    }


}