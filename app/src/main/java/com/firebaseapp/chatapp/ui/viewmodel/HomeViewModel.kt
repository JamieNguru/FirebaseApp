package com.firebaseapp.chatapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebaseapp.chatapp.data.FirebaseRepository
import com.firebaseapp.chatapp.model.Chat
import com.firebaseapp.chatapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class HomeUiState(
    val users: List<User> = emptyList(),
    val chats: List<Chat> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    private val latestMessagesMap = mutableMapOf<String, String>()
    private val latestTimestampsMap = mutableMapOf<String, Long>()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadUsers()
    }

    private fun loadUsers() {
        val currentUserId = repository.getCurrentUserId() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getAllUsers().collect { users ->
                // Load latest messages for each user
                users.forEach { user ->
                    viewModelScope.launch {
                        repository.getLatestMessage(currentUserId, user.uid).collect { message ->
                            latestMessagesMap[user.uid] = message?.message ?: ""
                            latestTimestampsMap[user.uid] = message?.timestamp ?: 0L
                            
                            // Build chats list
                            val chatsList = users.map { u ->
                                Chat(
                                    userId = u.uid,
                                    userName = u.name,
                                    userProfileImageUrl = u.profileImageUrl,
                                    lastMessage = latestMessagesMap[u.uid] ?: "",
                                    lastMessageTimestamp = latestTimestampsMap[u.uid] ?: 0L,
                                    isOnline = u.isOnline
                                )
                            }.sortedByDescending { it.lastMessageTimestamp }
                            
                            _uiState.value = _uiState.value.copy(
                                users = users,
                                chats = chatsList,
                                isLoading = false
                            )
                        }
                    }
                }
                
                // If no users, still update state
                if (users.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        users = users,
                        chats = emptyList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun logout() {
        repository.logout()
    }
}

