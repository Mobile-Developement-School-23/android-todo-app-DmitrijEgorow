package com.viable.tasklist.presentation.list

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(
    private val adapter: ItemAdapter,
    private val deleteItem: (id: String, position: Int) -> Unit,
    private val deleteIcon: Drawable,
    private val tickIcon: Drawable,
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val swipeBackgroundRight = ColorDrawable(Color.RED)
    private val swipeBackgroundLeft = ColorDrawable(Color.GREEN)

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (viewHolder.bindingAdapterPosition == adapter.itemCount - 1) {
            return 0
        }
        return super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            deleteItem(adapter.tasks[position].id, position)
        }
        if (direction == ItemTouchHelper.RIGHT) {
            adapter.setItemCompleted(true, position)
            viewHolder.itemView.translationX = 0f
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        // super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val iconMarginVertical = (itemView.height - deleteIcon.intrinsicHeight) / 2

        if (dX > 0) {
            swipeBackgroundLeft.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
            tickIcon.setBounds(
                itemView.left + iconMarginVertical,
                itemView.top + iconMarginVertical,
                itemView.left + iconMarginVertical + tickIcon.intrinsicWidth,
                itemView.bottom - iconMarginVertical,
            )
            swipeBackgroundLeft.draw(c)
            tickIcon.draw(c)

            // Limit the swipe distance
            val limitedDx = if (dX > itemView.width / 3) itemView.width / 3f else dX

            // Call the default onChildDraw method with the limited swipe distance
            super.onChildDraw(c, recyclerView, viewHolder, limitedDx, dY, actionState, isCurrentlyActive)
        } else {
            swipeBackgroundRight.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            deleteIcon.setBounds(
                itemView.right - iconMarginVertical - deleteIcon.intrinsicWidth,
                itemView.top + iconMarginVertical,
                itemView.right - iconMarginVertical,
                itemView.bottom - iconMarginVertical,
            )
            swipeBackgroundRight.draw(c)
            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
}
