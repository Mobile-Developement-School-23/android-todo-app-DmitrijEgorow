package com.viable.tasklist.presentation;

public interface ItemInteractionListener<T> {

    void onClick(int position, T item);
}
