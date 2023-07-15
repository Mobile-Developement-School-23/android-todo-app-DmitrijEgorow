package com.viable.tasklist.presentation.list

import android.content.res.Resources
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import java.text.SimpleDateFormat
import java.util.Locale

class DefaultItemFormatter(private val resources: Resources) : ItemFormatter {
    override fun formatDate(date: String?): CharSequence? {
        try {
            if (date != null) {
                val dateParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = dateParser.parse(date.substring(0, 19))
                val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
                if (date != null) {
                    return dateFormatter.format(date)
                }
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    override fun wrapWithTemplate(
        text: CharSequence?,
        stringId: Int,
        shouldStrikethrough: Boolean,
    ): CharSequence {
        if (text != null) {
            val content1 = resources.getString(stringId, text)
            val spannableString1 = SpannableString(content1)
            if (shouldStrikethrough) {
                spannableString1.setSpan(StrikethroughSpan(), 0, spannableString1.length, 0)
            }
            return spannableString1
        }

        return ""
    }
}
