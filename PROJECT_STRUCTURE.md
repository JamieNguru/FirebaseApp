# Complete Project Structure

## 📁 Directory Tree

```
FirebaseApp/
├── app/
│   ├── build.gradle.kts              # App-level Gradle configuration
│   ├── proguard-rules.pro            # ProGuard rules
│   ├── google-services.json          # Firebase configuration (replace with your file)
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml  # App manifest
│           ├── java/com/firebaseapp/chatapp/
│           │   ├── data/
│           │   │   └── FirebaseRepository.kt    # Firebase operations
│           │   ├── model/
│           │   │   ├── User.kt                  # User data model
│           │   │   ├── Message.kt               # Message data model
│           │   │   └── Chat.kt                  # Chat list item model
│           │   ├── ui/
│           │   │   ├── MainActivity.kt           # Main activity & navigation
│           │   │   ├── screens/
│           │   │   │   ├── LoginScreen.kt       # Login UI
│           │   │   │   ├── RegisterScreen.kt    # Registration UI
│           │   │   │   ├── HomeScreen.kt        # Users list UI
│           │   │   │   └── ChatScreen.kt        # Chat conversation UI
│           │   │   ├── viewmodel/
│           │   │   │   ├── AuthViewModel.kt      # Auth state management
│           │   │   │   ├── HomeViewModel.kt     # Home screen state
│           │   │   │   └── ChatViewModel.kt      # Chat screen state
│           │   │   └── theme/
│           │   │       └── Theme.kt              # Material Design theme
│           │   └── utils/
│           │       ├── Constants.kt              # App constants
│           │       └── Utils.kt                  # Utility functions
│           └── res/
│               ├── values/
│               │   ├── strings.xml               # String resources
│               │   └── themes.xml                # XML themes
│               ├── xml/
│               │   ├── backup_rules.xml         # Backup rules
│               │   └── data_extraction_rules.xml # Data extraction rules
│               └── mipmap/                       # App icons
├── build.gradle.kts                 # Project-level Gradle
├── settings.gradle.kts              # Gradle settings
├── gradle.properties                # Gradle properties
├── firebase-database-rules.json     # Firebase Realtime Database rules
├── FIREBASE_SETUP.md                # Firebase setup guide
├── PROJECT_STRUCTURE.md             # This file
└── README.md                        # Project README
```

## 📋 File Descriptions

### Core Application Files

#### **MainActivity.kt**
- Entry point of the application
- Handles navigation between screens
- Checks authentication state on startup

#### **FirebaseRepository.kt**
- Centralized Firebase operations
- Handles authentication (register, login, logout)
- Manages user data (getAllUsers, getUser)
- Handles messaging (sendMessage, getMessages, getLatestMessage)
- Manages online/offline status

### Data Models

#### **User.kt**
- Represents a user in the system
- Fields: uid, name, email, profileImageUrl, isOnline
- Includes toMap() and fromMap() for Firebase serialization

#### **Message.kt**
- Represents a chat message
- Fields: messageId, message, senderId, receiverId, timestamp
- Includes toMap() and fromMap() for Firebase serialization

#### **Chat.kt**
- Represents a chat item in the home screen list
- Fields: userId, userName, userProfileImageUrl, lastMessage, lastMessageTimestamp, isOnline

### UI Screens (Jetpack Compose)

#### **LoginScreen.kt**
- Email and password input fields
- Login button with loading state
- Error message display
- Navigation to register screen

#### **RegisterScreen.kt**
- Name, email, and password input fields
- Registration button with loading state
- Error message display
- Navigation to login screen

#### **HomeScreen.kt**
- Displays list of all users (except current user)
- Shows profile images, names, online status
- Shows latest message preview
- Click to navigate to chat screen
- Logout button in app bar

#### **ChatScreen.kt**
- Displays conversation messages
- Message input field with send button
- Auto-scrolls to latest message
- Shows receiver info in app bar
- Real-time message updates

### ViewModels

#### **AuthViewModel.kt**
- Manages authentication state
- Handles registration and login logic
- Error handling and loading states

#### **HomeViewModel.kt**
- Manages home screen state
- Loads users list
- Loads latest messages for each user
- Handles logout

#### **ChatViewModel.kt**
- Manages chat screen state
- Loads messages for a conversation
- Handles sending messages
- Real-time message updates

### Utilities

#### **Constants.kt**
- Database path constants
- App-wide constants

#### **Utils.kt**
- Message ID generation
- Timestamp formatting

### Configuration Files

#### **build.gradle.kts** (app level)
- App dependencies
- Compose configuration
- Firebase dependencies
- Build configuration

#### **AndroidManifest.xml**
- App permissions (Internet, Network State)
- Activity declarations
- Application configuration

#### **firebase-database-rules.json**
- Security rules for Realtime Database
- User data access rules
- Chat message access rules
- Status access rules

## 🔄 Data Flow

1. **Authentication Flow**
   - User enters credentials → AuthViewModel → FirebaseRepository → Firebase Auth
   - On success → Navigate to HomeScreen

2. **Home Screen Flow**
   - HomeViewModel loads users → FirebaseRepository.getAllUsers()
   - For each user → Load latest message → Update chat list
   - User clicks → Navigate to ChatScreen with userId

3. **Chat Flow**
   - ChatViewModel loads messages → FirebaseRepository.getMessages()
   - User types message → ChatViewModel.sendMessage() → FirebaseRepository
   - Real-time updates via Flow → UI updates automatically

## 🎨 UI Architecture

- **Jetpack Compose** for all UI
- **Material Design 3** components
- **Navigation Compose** for screen navigation
- **StateFlow** for reactive state management
- **ViewModel** for business logic separation

## 🔐 Security

- Firebase Authentication for user verification
- Database rules restrict access to user's own data
- Messages can only be read/written by sender or receiver
- Online status can only be updated by the user themselves

## 📱 Features Implementation

✅ User Authentication (Email + Password)
✅ User Registration
✅ User Profile Storage
✅ Home Screen with User List
✅ Online/Offline Status
✅ Realtime Chat
✅ Latest Message Preview
✅ Message Timestamps
✅ Profile Images (structure ready, upload optional)

