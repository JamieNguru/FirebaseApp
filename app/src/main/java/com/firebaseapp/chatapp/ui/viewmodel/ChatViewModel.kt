package com.firebaseapp.chatapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebaseapp.chatapp.data.FirebaseRepository
import com.firebaseapp.chatapp.model.Message
import com.firebaseapp.chatapp.model.User
import com.firebaseapp.chatapp.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val receiver: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ChatViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    fun loadChat(receiverId: String) {
        val senderId = repository.getCurrentUserId() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Load receiver info
            repository.getUser(receiverId).collect { user ->
                _uiState.value = _uiState.value.copy(receiver = user)
            }

            // Load messages
            repository.getMessages(senderId, receiverId).collect { messages ->
                _uiState.value = _uiState.value.copy(
                    messages = messages,
                    isLoading = false
                )
            }
        }
    }

    fun sendMessage(messageText: String) {
        val senderId = repository.getCurrentUserId() ?: return
        val receiverId = _uiState.value.receiver?.uid ?: return

        if (messageText.isBlank()) return

        val message = Message(
            messageId = Utils.generateMessageId(),
            message = messageText.trim(),
            senderId = senderId,
            receiverId = receiverId,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.sendMessage(message)
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = exception.message ?: "Failed to send message"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

