package com.viable.tasklist.presentation

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.viable.tasklist.R
import com.viable.tasklist.data.Importance
import com.viable.tasklist.data.TodoItem

class ItemViewHolder(
    itemView: View,
    private val itemProceedListener: ItemInteractionListener<TodoItem>,
    private val formatter: ItemFormatter,
) : RecyclerView.ViewHolder(itemView) {

    private val itemCheckBox: CheckBox = itemView.findViewById(R.id.task_checkbox)
    private val itemTitle: TextView = itemView.findViewById(R.id.task_title)
    private var itemPriority: ImageButton? = itemView.findViewById(R.id.task_priority)
    private val itemDeadline: MaterialButton = itemView.findViewById(R.id.task_deadline)
    private val itemProceed: ImageButton = itemView.findViewById(R.id.item_proceed)

    internal var completionListener: ItemCompletionListener? = null
        set(value) {
            field = value
        }

    fun onBind(preview: TodoItem) {
        val isCompletedItem = preview.isCompleted
        itemTitle.text = formatter.wrapWithTemplate(
            preview.text,
            R.string.task_template_done,
            isCompletedItem,
        )
        itemDeadline.text = formatter.wrapWithTemplate(
            formatter.formatDate(if (preview.deadline != null) preview.deadline.toString() else null),
            R.string.task_template_done,
            isCompletedItem,
        )

        if (preview.deadline == null) {
            itemDeadline.visibility = View.GONE
        }
        itemCheckBox.isChecked = isCompletedItem
        itemProceed.setOnClickListener {
            itemProceedListener.onClick(this.bindingAdapterPosition, preview)
        }
        itemTitle.setOnClickListener {
        }

        when (preview.importance) {
            Importance.LOW -> {
                itemPriority?.setImageResource(R.drawable.priority_low)
            }

            Importance.URGENT -> {
                itemPriority?.setImageResource(R.drawable.priority_high)
            }

            else ->
                itemPriority?.visibility = View.INVISIBLE
        }

        itemCheckBox.setOnClickListener { view ->
            if ((view as CheckBox).isChecked) {
                preview.isCompleted = true
                completionListener?.onItemStatusChanged(true)
            } else {
                preview.isCompleted = false
                completionListener?.onItemStatusChanged(false)
            }
        }
    }
}
