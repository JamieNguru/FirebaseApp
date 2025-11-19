# Firebase Realtime Chat Application

A complete Android chat application built with Kotlin, Jetpack Compose, and Firebase Realtime Database.

## Features

✅ **User Authentication**
- Email/Password registration and login
- User profile management
- Secure authentication with Firebase Auth

✅ **Home/Chats Screen**
- List of all registered users (excluding current user)
- Shows profile pictures, names, and online/offline status
- Displays latest message preview
- Click on any user to start chatting

✅ **Realtime Chat**
- Two-way realtime messaging
- Messages sync instantly across devices
- Message timestamps
- Auto-scroll to latest message
- Beautiful chat bubble UI

✅ **Additional Features**
- Online/Offline status tracking
- Latest message preview on home screen
- Formatted timestamps (Just now, 5m ago, etc.)
- Profile image support (via Firebase Storage - optional)

## Project Structure

```
app/src/main/java/com/firebaseapp/chatapp/
├── data/
│   └── FirebaseRepository.kt      # Firebase data operations
├── model/
│   ├── User.kt                     # User data model
│   ├── Message.kt                  # Message data model
│   └── Chat.kt                     # Chat list item model
├── ui/
│   ├── MainActivity.kt             # Main activity with navigation
│   ├── screens/
│   │   ├── LoginScreen.kt          # Login UI
│   │   ├── RegisterScreen.kt       # Registration UI
│   │   ├── HomeScreen.kt           # Users list UI
│   │   └── ChatScreen.kt           # Chat conversation UI
│   ├── viewmodel/
│   │   ├── AuthViewModel.kt        # Authentication logic
│   │   ├── HomeViewModel.kt        # Home screen logic
│   │   └── ChatViewModel.kt        # Chat screen logic
│   └── theme/
│       └── Theme.kt                # App theme
└── utils/
    ├── Constants.kt                # App constants
    └── Utils.kt                    # Utility functions
```

## Setup Instructions

### 1. Firebase Project Setup

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Enable **Authentication** (Email/Password)
3. Create a **Realtime Database**
4. Add Android app with package: `com.firebaseapp.chatapp`
5. Download `google-services.json` and replace `app/google-services.json`

### 2. Database Rules

Apply the rules from `firebase-database-rules.json` to your Realtime Database:
- Go to Realtime Database > Rules
- Paste the rules
- Publish

### 3. Build and Run

```bash
./gradlew build
./gradlew installDebug
```

Or use Android Studio:
1. Open the project
2. Sync Gradle files
3. Run the app

## Database Structure

```
{
  "users": {
    "{userId}": {
      "uid": "string",
      "name": "string",
      "email": "string",
      "profileImageUrl": "string",
      "isOnline": "boolean"
    }
  },
  "chats": {
    "{senderId}": {
      "{receiverId}": {
        "{messageId}": {
          "messageId": "string",
          "message": "string",
          "senderId": "string",
          "receiverId": "string",
          "timestamp": "number"
        }
      }
    }
  },
  "status": {
    "{userId}": {
      "isOnline": "boolean"
    }
  }
}
```

## Technologies Used

- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Firebase Authentication** - User authentication
- **Firebase Realtime Database** - Realtime data synchronization
- **Firebase Storage** - Profile images (optional)
- **Material Design 3** - UI components
- **Coil** - Image loading
- **Coroutines & Flow** - Asynchronous programming
- **ViewModel** - UI state management
- **Navigation Compose** - Screen navigation

## Requirements

- Android Studio Hedgehog or later
- Min SDK: 24
- Target SDK: 34
- Kotlin 1.9.20+
- JDK 8+

## Usage

1. **Register**: Create a new account with email and password
2. **Login**: Sign in with your credentials
3. **Browse Users**: See all registered users on the home screen
4. **Start Chatting**: Tap on any user to open the chat screen
5. **Send Messages**: Type and send messages in real-time
6. **Logout**: Use the logout button in the top bar

## Notes

- Messages are stored in both directions for easy retrieval
- Online status updates automatically when users login/logout
- The app requires internet connection
- Profile images are optional (can be added via Firebase Storage)

## Troubleshooting

- **Authentication errors**: Ensure Email/Password is enabled in Firebase Console
- **Database errors**: Check that database rules are correctly applied
- **Connection issues**: Verify `google-services.json` is properly configured

For detailed Firebase setup, see `FIREBASE_SETUP.md`.

## License

This project is open source and available for educational purposes.
