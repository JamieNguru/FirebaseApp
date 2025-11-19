package com.firebaseapp.chatapp.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val isOnline: Boolean = false
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "profileImageUrl" to profileImageUrl,
            "isOnline" to isOnline
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): User {
            return User(
                uid = map["uid"] as? String ?: "",
                name = map["name"] as? String ?: "",
                email = map["email"] as? String ?: "",
                profileImageUrl = map["profileImageUrl"] as? String ?: "",
                isOnline = map["isOnline"] as? Boolean ?: false
            )
        }
    }
}

