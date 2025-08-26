# Frontend Setup

## Requirements

- [Android Studio](https://developer.android.com/studio) (latest version)
- [Java 11](https://adoptium.net/) or higher
- [Android SDK](https://developer.android.com/studio#command-tools) with API level 31+ (Android 12)
- [Kotlin](https://kotlinlang.org/) 2.0.0
- [Gradle](https://gradle.org/) 8.6.1+

## Setup

1. **Open project**: Open the project in Android Studio
2. **Sync Gradle**: Click "Sync Now" when prompted, or run `./gradlew build`
3. **Configure Android SDK**: Ensure you have Android SDK 34 (API level 34) installed
4. **Set up emulator/device**:
   - Use Android Studio's AVD Manager to create a Pixel 9 emulator with API 31+
   - Or connect a physical Android device with API 31+

## Build and Run

- **Setup app config**: Add app config using the template in `app/src/main/java/com/cpen321/usermanagement/config/AppConfig.template.kt`
- **Debug build**: Click the green play button in Android Studio, or run `./gradlew assembleDebug`
- **Release build**: `./gradlew assembleRelease`
- **Install on device**: `./gradlew installDebug`

## Available Gradle Tasks

- `./gradlew build` - Build the entire project
- `./gradlew clean` - Clean build outputs
- `./gradlew assembleDebug` - Build debug APK
- `./gradlew assembleRelease` - Build release APK
- `./gradlew installDebug` - Install debug APK on connected device

## Key Features

- **Jetpack Compose** UI framework
- **Google Sign-In** authentication
- **Camera integration** for profile pictures
- **HTTP client** with Retrofit for backend communication
- **Image loading** with Coil
- **Navigation** with Navigation Compose
- **ViewModel** architecture with Compose

## Permissions

The app requires:

- Internet access for backend communication
- Camera access for profile pictures
- Storage access for image handling

## Backend Configuration

Ensure the backend server is running and update the base URL in the app configuration if needed.
