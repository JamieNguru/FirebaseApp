# Firebase Setup Guide

## 1. Firebase Project Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. Enable the following services:
   - **Authentication** (Email/Password)
   - **Realtime Database**
   - **Storage** (optional, for profile images)

## 2. Android App Configuration

1. In Firebase Console, go to Project Settings
2. Add an Android app with package name: `com.firebaseapp.chatapp`
3. Download `google-services.json`
4. Replace the placeholder `app/google-services.json` file with your downloaded file

## 3. Enable Authentication

1. Go to **Authentication** > **Sign-in method**
2. Enable **Email/Password** provider

## 4. Realtime Database Setup

1. Go to **Realtime Database** > **Create Database**
2. Choose your preferred location
3. Start in **test mode** initially (or use the rules below)
4. Copy the database URL

### Database Rules

Apply the rules from `firebase-database-rules.json` to your Realtime Database:

1. Go to **Realtime Database** > **Rules**
2. Paste the rules from `firebase-database-rules.json`
3. Click **Publish**

## 5. Database Structure

The app uses the following structure:

```
{
  "users": {
    "{userId}": {
      "uid": "string",
      "name": "string",
      "email": "string",
      "profileImageUrl": "string (optional)",
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

### Notes:
- Messages are stored in both directions: `/chats/{senderId}/{receiverId}` and `/chats/{receiverId}/{senderId}`
- This allows both users to see the same conversation
- Online status is tracked separately in `/status/{userId}/isOnline`

## 6. Firebase Storage (Optional)

If you want to enable profile image uploads:

1. Go to **Storage** > **Get Started**
2. Set up security rules:
```json
{
  "rules": {
    "profile_images": {
      "$userId": {
        ".read": "auth != null",
        ".write": "$userId === auth.uid",
        ".validate": "request.resource.size < 5 * 1024 * 1024"
      }
    }
  }
}
```

## 7. Update Repository (if using Storage)

If you enable Storage, you'll need to add image upload functionality to `FirebaseRepository.kt`:

```kotlin
suspend fun uploadProfileImage(userId: String, imageUri: Uri): Result<String> {
    return try {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/$userId.jpg")
        val uploadTask = imageRef.putFile(imageUri).await()
        val downloadUrl = uploadTask.storage.downloadUrl.await()
        Result.success(downloadUrl.toString())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

## 8. Testing

1. Build and run the app
2. Register a new user
3. Register another user on a different device/emulator
4. Start chatting!

## Troubleshooting

- **Authentication errors**: Make sure Email/Password is enabled in Firebase Console
- **Database permission errors**: Check that the rules are correctly applied
- **Connection issues**: Verify `google-services.json` is correctly placed and configured

