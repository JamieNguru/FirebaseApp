# Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Firebase Setup (2 minutes)

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **"Add Project"** or select existing project
3. Enable **Authentication** → **Sign-in method** → Enable **Email/Password**
4. Create **Realtime Database** → Choose location → Start in **test mode**
5. Add Android app:
   - Package name: `com.firebaseapp.chatapp`
   - Download `google-services.json`
   - Replace `app/google-services.json` in the project

### Step 2: Apply Database Rules (1 minute)

1. In Firebase Console → **Realtime Database** → **Rules**
2. Copy content from `firebase-database-rules.json`
3. Paste and click **Publish**

### Step 3: Build & Run (2 minutes)

#### Option A: Android Studio
1. Open project in Android Studio
2. Wait for Gradle sync
3. Click **Run** ▶️

#### Option B: Command Line
```bash
./gradlew assembleDebug
./gradlew installDebug
```

### Step 4: Test the App

1. **Register** a new user:
   - Email: `user1@test.com`
   - Password: `password123`
   - Name: `User One`

2. **Register** another user (on different device/emulator):
   - Email: `user2@test.com`
   - Password: `password123`
   - Name: `User Two`

3. **Start Chatting**:
   - Login as User One
   - See User Two in the list
   - Tap on User Two
   - Send messages!

## ✅ Verification Checklist

- [ ] Firebase project created
- [ ] Authentication enabled (Email/Password)
- [ ] Realtime Database created
- [ ] `google-services.json` added to `app/` folder
- [ ] Database rules applied
- [ ] App builds successfully
- [ ] Can register new user
- [ ] Can login
- [ ] Can see other users
- [ ] Can send/receive messages

## 🐛 Common Issues

### "google-services.json not found"
- Make sure the file is in `app/google-services.json`
- Verify package name matches: `com.firebaseapp.chatapp`

### "Authentication failed"
- Check Email/Password is enabled in Firebase Console
- Verify internet connection

### "Permission denied" in database
- Apply database rules from `firebase-database-rules.json`
- Check rules are published

### "No users showing"
- Register at least 2 users
- Make sure you're logged in
- Check internet connection

## 📝 Next Steps

- Customize UI colors in `Theme.kt`
- Add profile image upload (see `FIREBASE_SETUP.md`)
- Add push notifications
- Add message read receipts
- Add typing indicators

## 💡 Tips

- Use two devices/emulators to test real-time chat
- Check Firebase Console → Realtime Database to see data
- Monitor Firebase Console → Authentication to see users
- Use Firebase Console → Realtime Database → Data to debug

## 📚 Documentation

- **README.md** - Full project documentation
- **FIREBASE_SETUP.md** - Detailed Firebase setup
- **PROJECT_STRUCTURE.md** - Complete file structure

Happy Coding! 🎉

