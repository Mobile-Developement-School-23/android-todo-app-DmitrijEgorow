package com.viable.tasklist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.viable.tasklist.R
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.domain.CommonCallbackImpl

class ItemAdapter(
    private val interactionListener: ItemInteractionListener<TodoItem>,
    private val taskCompletionListener: ItemInteractionListener<Boolean>,
    private val formatter: ItemFormatter,
) : RecyclerView.Adapter<ViewHolder>() {

    var tasks: MutableList<TodoItem> = arrayListOf<TodoItem>()
        set(value) {
            val callback = CommonCallbackImpl(
                oldItems = field,
                newItems = value,
                { oldItem: TodoItem, newItem -> oldItem.id == newItem.id },
            )
            field = value
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
        }

    override fun getItemViewType(position: Int): Int {
        if (position == tasks.size - 1) {
            return NEW_ITEM_TYPE
        }
        return DEFAULT_ITEM_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val holder = when (viewType) {
            DEFAULT_ITEM_TYPE -> ItemViewHolder(
                layoutInflater.inflate(
                    R.layout.item_layout,
                    parent,
                    false,
                ),
                itemProceedListener = interactionListener,
                formatter = formatter,
            )

            NEW_ITEM_TYPE -> ItemViewHolder(
                layoutInflater.inflate(
                    R.layout.new_item_layout,
                    parent,
                    false,
                ),
                itemProceedListener = interactionListener,
                formatter = formatter,
            )

            else -> throw java.lang.IllegalArgumentException("viewType is invalid")
        }

        holder.itemView.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_FirstFragment_to_SecondFragment,
            )
        }

        return holder
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                holder.onBind(tasks[position])
                val completionListener = ItemCompletionListener { isCompleted ->
                    // setItemCompleted(isCompleted, position)
                    taskCompletionListener.onClick(position, isCompleted)
                }
                holder.completionListener = completionListener
            }
        }
    }

    fun removeItem(position: Int): TodoItem {
        var item: TodoItem
        tasks = tasks.also { list ->
            item = list.removeAt(position)
        }
        notifyItemRemoved(position)
        return item
    }

    fun addItem(position: Int, item: TodoItem) {
        tasks = tasks.apply {
            add(position, item)
        }
        notifyItemInserted(position)
    }

    fun setItemCompleted(isCompleted: Boolean, position: Int) {
        tasks[position].isCompleted = isCompleted
        notifyItemChanged(position)
    }

    companion object {
        private const val NEW_ITEM_TYPE = 0
        private const val DEFAULT_ITEM_TYPE = 1
    }
}
