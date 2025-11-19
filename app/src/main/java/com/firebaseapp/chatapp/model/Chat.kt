package com.firebaseapp.chatapp.model

data class Chat(
    val userId: String = "",
    val userName: String = "",
    val userProfileImageUrl: String = "",
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = 0L,
    val isOnline: Boolean = false
)

