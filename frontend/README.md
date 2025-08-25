# User Management Android App

A native Android application built with Kotlin and Jetpack Compose for user authentication and profile management.

## Features

- **Google OAuth Authentication**: Secure sign-in using Google credentials
- **Automatic User Registration**: New users are automatically registered on first login
- **Profile Management**: View user profile information including name, email, and profile picture
- **Secure Logout**: Complete logout with token cleanup
- **Modern UI**: Built with Jetpack Compose and Material Design 3
- **Error Handling**: Comprehensive error handling for network and authentication failures

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with ViewModels
- **Navigation**: Navigation Compose
- **HTTP Client**: Retrofit with OkHttp
- **Image Loading**: Coil
- **Authentication**: Google Sign-In SDK
- **Data Storage**: DataStore (for token management)
- **Minimum SDK**: API 31 (Android 12)
- **Target SDK**: API 34

## Prerequisites

- Android Studio (latest version)
- Android SDK API 31 or higher
- Google Play Services
- Running backend server (see backend README)

## Setup Instructions

### 1. Google OAuth Configuration

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the Google Sign-In API
4. Create OAuth 2.0 credentials:
   - Go to "Credentials" → "Create Credentials" → "OAuth 2.0 Client IDs"
   - Select "Android" as application type
   - Add your package name: `com.cpen321.usermanagement`
   - Add your SHA-1 certificate fingerprint (for development, use debug keystore)
5. Copy the Client ID

### 2. Configure the App

1. Open `AuthRepository.kt` file
2. Replace `"YOUR_GOOGLE_CLIENT_ID"` with your actual Google Client ID:
   ```kotlin
   private val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
       .requestIdToken("YOUR_ACTUAL_GOOGLE_CLIENT_ID") // Replace this
       .requestEmail()
       .build()
   ```

### 3. Backend Configuration

1. Ensure the backend server is running (see backend README)
2. If testing on a real device, update the BASE_URL in `RetrofitClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://YOUR_LOCAL_IP:3000/api/"
   ```
   Replace `YOUR_LOCAL_IP` with your computer's local IP address

### 4. Build and Run

1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Connect an Android device or start an emulator (API 31+)
4. Run the app

## Project Structure

```
android/
├── app/
│   ├── src/main/java/com/cpen321/usermanagement/
│   │   ├── data/
│   │   │   ├── api/           # API service and Retrofit setup
│   │   │   ├── model/         # Data models
│   │   │   ├── repository/    # Repository pattern implementation
│   │   │   └── storage/       # Local data storage
│   │   ├── ui/
│   │   │   ├── navigation/    # Navigation setup
│   │   │   ├── screen/        # Compose screens
│   │   │   ├── theme/         # Material Design theme
│   │   │   └── viewmodel/     # ViewModels for state management
│   │   └── MainActivity.kt    # Main activity
│   └── src/main/res/          # Android resources
├── build.gradle.kts           # App-level Gradle configuration
└── README.md                  # This file
```

## API Integration

The app communicates with the backend using the following endpoints:

- `POST /api/auth/google` - Google OAuth authentication
- `GET /api/profile` - Retrieve user profile
- `POST /api/auth/logout` - User logout

## Key Components

### Authentication Flow
1. User taps "Sign in with Google"
2. Google Sign-In SDK handles OAuth flow
3. App receives Google ID token
4. Token is sent to backend for verification
5. Backend returns JWT token and user data
6. JWT token is stored locally for authenticated requests

### State Management
- `AuthViewModel`: Manages authentication state
- `ProfileViewModel`: Manages profile data
- Uses Kotlin StateFlow for reactive UI updates

### Navigation
- Navigation Compose handles screen transitions
- Automatic navigation based on authentication state
- Back stack management for proper user flow

## Testing

### Emulator Testing
- Use the default emulator configuration
- Backend URL is pre-configured for emulator (10.0.2.2)

### Real Device Testing
1. Connect device via USB or WiFi debugging
2. Update BASE_URL in RetrofitClient.kt with your local IP
3. Ensure device and computer are on the same network
4. Install and run the app

## Troubleshooting

### Common Issues

1. **Google Sign-In not working**
   - Verify Google Client ID is correctly configured
   - Check SHA-1 fingerprint matches in Google Console
   - Ensure Google Play Services is installed

2. **Network requests failing**
   - Verify backend server is running
   - Check BASE_URL configuration for real device testing
   - Ensure INTERNET permission is granted

3. **Build errors**
   - Clean and rebuild project
   - Invalidate caches and restart Android Studio
   - Check Gradle sync issues

### Getting SHA-1 Fingerprint

For debug builds:
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

## Security Considerations

- JWT tokens are stored securely using DataStore
- Network communication uses HTTPS in production
- Google OAuth provides secure authentication
- Tokens are automatically cleared on logout

## Future Enhancements

- Biometric authentication
- Offline support
- Profile editing capabilities
- Push notifications
- Dark mode support
