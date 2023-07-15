package com.viable.tasklist.presentation.list

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.viable.tasklist.R
import com.viable.tasklist.data.TodoItem

class DeleteSnackbarHelper(
    private val context: Context,
    private val rootView: View,
    private val layoutInflater: LayoutInflater,
    private val adapter: ItemAdapter,
    private val item: TodoItem
) {

    private val snackbarMaxTextLength = 7

    fun onDeleteSnackbarRequest(id: String, position: Int): Snackbar {

        val snackbarText = context.getString(
            R.string.one_task_deleted,
            item.text.substring(0, minOf(item.text.length, snackbarMaxTextLength))
        )
        val snackbar = Snackbar.make(
            rootView,
            snackbarText,
            Snackbar.LENGTH_INDEFINITE,
        ).setAction(context.getString(R.string.undo_delete)) {
                adapter.addItem(position, item)
            }

        val snackbarLayout = snackbar.view
        val textView =
            snackbarLayout.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        val layout = snackbar.view as Snackbar.SnackbarLayout
        textView.visibility = View.INVISIBLE
        val snackView: View = layoutInflater.inflate(R.layout.snackbar_layout, null)
        val textViewTop = snackView.findViewById<View>(R.id.text) as TextView
        textViewTop.text = snackbarText
        val image = snackView.findViewById<View>(R.id.image) as ImageView
        val animation = image.drawable as AnimationDrawable
        animation.start()
        layout.addView(snackView, 0)

        return snackbar


    }
}