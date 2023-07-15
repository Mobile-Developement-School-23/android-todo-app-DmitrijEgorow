package com.viable.tasklist.presentation.list;

public interface ItemInteractionListener<T> {

    void onClick(int position, T item);
}
