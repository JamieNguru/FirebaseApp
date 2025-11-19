package com.firebaseapp.chatapp.data

import com.firebaseapp.chatapp.model.Message
import com.firebaseapp.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    // Authentication
    suspend fun registerUser(email: String, password: String, name: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("User creation failed"))
            
            // Save user profile
            val user = User(
                uid = uid,
                name = name,
                email = email,
                profileImageUrl = "",
                isOnline = true
            )
            database.child("users").child(uid).setValue(user.toMap()).await()
            
            // Set online status
            database.child("status").child(uid).child("isOnline").setValue(true).await()
            
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Login failed"))
            
            // Update online status
            database.child("status").child(uid).child("isOnline").setValue(true).await()
            
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        val uid = auth.currentUser?.uid
        auth.signOut()
        if (uid != null) {
            database.child("status").child(uid).child("isOnline").setValue(false)
        }
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // Users
    fun getAllUsers(): Flow<List<User>> = callbackFlow {
        val currentUserId = getCurrentUserId() ?: run {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val usersMap = mutableMapOf<String, User>()
        
        val usersListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    val user = User.fromMap(child.value as Map<String, Any>)
                    if (user.uid != currentUserId) {
                        usersMap[user.uid] = user
                        // Get online status for this user
                        database.child("status").child(user.uid).child("isOnline")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(statusSnapshot: DataSnapshot) {
                                    val isOnline = statusSnapshot.getValue(Boolean::class.java) ?: false
                                    usersMap[user.uid] = user.copy(isOnline = isOnline)
                                    trySend(usersMap.values.toList())
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        // Also listen to status changes
        val statusListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { statusChild ->
                    val userId = statusChild.key ?: return@forEach
                    if (userId != currentUserId && usersMap.containsKey(userId)) {
                        val isOnline = statusChild.child("isOnline").getValue(Boolean::class.java) ?: false
                        val user = usersMap[userId]
                        if (user != null) {
                            usersMap[userId] = user.copy(isOnline = isOnline)
                            trySend(usersMap.values.toList())
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        database.child("users").addValueEventListener(usersListener)
        database.child("status").addValueEventListener(statusListener)

        awaitClose {
            database.child("users").removeEventListener(usersListener)
            database.child("status").removeEventListener(statusListener)
        }
    }

    fun getUser(userId: String): Flow<User?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = User.fromMap(snapshot.value as Map<String, Any>)
                    database.child("status").child(userId).child("isOnline")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(statusSnapshot: DataSnapshot) {
                                val isOnline = statusSnapshot.getValue(Boolean::class.java) ?: false
                                trySend(user.copy(isOnline = isOnline))
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                } else {
                    trySend(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.child("users").child(userId).addValueEventListener(listener)

        awaitClose {
            database.child("users").child(userId).removeEventListener(listener)
        }
    }

    // Messages
    suspend fun sendMessage(message: Message): Result<Unit> {
        return try {
            val senderId = message.senderId
            val receiverId = message.receiverId
            
            // Save message in both directions
            database.child("chats").child(senderId).child(receiverId)
                .child(message.messageId).setValue(message.toMap()).await()
            
            database.child("chats").child(receiverId).child(senderId)
                .child(message.messageId).setValue(message.toMap()).await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getMessages(senderId: String, receiverId: String): Flow<List<Message>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                snapshot.children.forEach { child ->
                    val message = Message.fromMap(child.value as Map<String, Any>)
                    messages.add(message)
                }
                // Sort by timestamp
                trySend(messages.sortedBy { it.timestamp })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.child("chats").child(senderId).child(receiverId)
            .addValueEventListener(listener)

        awaitClose {
            database.child("chats").child(senderId).child(receiverId)
                .removeEventListener(listener)
        }
    }

    // Get latest message for chat list
    fun getLatestMessage(senderId: String, receiverId: String): Flow<Message?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var latestMessage: Message? = null
                var latestTimestamp = 0L
                
                snapshot.children.forEach { child ->
                    val message = Message.fromMap(child.value as Map<String, Any>)
                    if (message.timestamp > latestTimestamp) {
                        latestTimestamp = message.timestamp
                        latestMessage = message
                    }
                }
                
                trySend(latestMessage)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.child("chats").child(senderId).child(receiverId)
            .addValueEventListener(listener)

        awaitClose {
            database.child("chats").child(senderId).child(receiverId)
                .removeEventListener(listener)
        }
    }

    // Update online status
    fun updateOnlineStatus(userId: String, isOnline: Boolean) {
        database.child("status").child(userId).child("isOnline").setValue(isOnline)
    }
}

