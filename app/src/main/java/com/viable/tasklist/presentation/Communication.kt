package com.viable.tasklist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface Communication<T> {
    fun map(data: T)
    fun observe(owner: LifecycleOwner, observer: Observer<T>)
    class Base<T : Any> : Communication<T> {
        override fun map(data: T) {
            // liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {}
        // liveData.observe(owner, observer)
    }
}
