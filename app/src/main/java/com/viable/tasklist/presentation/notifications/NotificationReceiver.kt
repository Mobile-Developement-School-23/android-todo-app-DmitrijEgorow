package com.viable.tasklist.presentation.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    private val titleLabel = "Title"
    private val contentLabel = "Content"

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(titleLabel)
        val content = intent.getStringExtra(contentLabel)
        if (title != null && content != null) {
            NotificationHelper(context).dispatchNotification(title, content)
        }
    }
}
