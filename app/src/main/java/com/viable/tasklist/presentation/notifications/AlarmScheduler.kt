package com.viable.tasklist.presentation.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.viable.tasklist.R
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.presentation.list.DefaultItemFormatter
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import kotlin.random.Random


class AlarmScheduler(private val context: Context) {

    fun scheduleAlarm(item: TodoItem) {
        /*val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 3)
            set(Calendar.MINUTE, 3)
            set(Calendar.SECOND, 1)
        }*/

        if (item.deadline != null) {
            val deadline = item.deadline
            val zoneId = ZoneId.systemDefault()
            Log.d("gsonon zone", zoneId.toString())
            val date = Date.from(deadline.atZone(zoneId).toInstant())
            val calendar = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 3)
                set(Calendar.MINUTE, 3)
                set(Calendar.SECOND, 1)
            }
            Log.d("gsonon zone", calendar.time.toString())
            val formatter = DefaultItemFormatter(context.resources)
            val formattedDeadline = formatter.wrapWithTemplate(
                formatter.formatDate(deadline.toString()),
                R.string.task_template_with_deadline_text, false
            ).toString()
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            Log.d("gsonon zone", formattedDeadline)
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("Title", item.text)
                putExtra("Content", formattedDeadline)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, Random.nextInt(), intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

    }
}
