package com.firebaseapp.chatapp.model

data class Message(
    val messageId: String = "",
    val message: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "messageId" to messageId,
            "message" to message,
            "senderId" to senderId,
            "receiverId" to receiverId,
            "timestamp" to timestamp
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Message {
            return Message(
                messageId = map["messageId"] as? String ?: "",
                message = map["message"] as? String ?: "",
                senderId = map["senderId"] as? String ?: "",
                receiverId = map["receiverId"] as? String ?: "",
                timestamp = (map["timestamp"] as? Long) ?: System.currentTimeMillis()
            )
        }
    }
}

