package com.firebaseapp.chatapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun generateMessageId(): String {
        return System.currentTimeMillis().toString() + (0..999).random().toString()
    }

    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val now = Date()
        val diff = now.time - timestamp
        
        return when {
            diff < 60000 -> "Just now" // Less than 1 minute
            diff < 3600000 -> "${diff / 60000}m ago" // Less than 1 hour
            diff < 86400000 -> SimpleDateFormat("h:mm a", Locale.getDefault()).format(date) // Today
            diff < 604800000 -> SimpleDateFormat("EEE h:mm a", Locale.getDefault()).format(date) // This week
            else -> SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(date) // Older
        }
    }
}

