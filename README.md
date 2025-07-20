# SimpleFirebaseAuth

A modern, minimal Firebase authentication sample app built with Android, Kotlin, and Views libraries. Easily sign up, log in, and manage users with Firebase Authentication and a clean, user-friendly UI.

## Features

- Email/password registration and login
- Real-time input validation with helpful error messages
- Smooth UI transitions and loading indicators
- User authentication via Firebase Auth
- MVVM architecture using ViewModel and StateFlow
- Clean, modern Material UI

## Optimizations & Architecture

- **Dependency Injection:** Uses Koin for ViewModel injection and lifecycle management.
- **Coroutines & StateFlow:** UI state is managed reactively with StateFlow and coroutines for robust, responsive updates.
- **Resource Optimization:** Release builds use ProGuard for minification and resource shrinking.
- **UI/UX:** Material Components for consistent look and feel, with native dialogs for errors and confirmations.
- **Loading State:** Custom overlay and progress indicator block UI during network operations.

## Getting Started

### Prerequisites

- JDK 17+
- Android Studio (latest recommended)
- Gradle (wrapper included)
- Firebase project (for Auth)

### Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/<your-username>/SimpleFirebaseAuth.git
   cd SimpleFirebaseAuth
   ```
2. **Configure Firebase:**
   - Go to [Firebase Console](https://console.firebase.google.com/), create a project.
   - Download `google-services.json` and place it in `app/`.
   - Enable Email/Password authentication in Firebase Console > Authentication > Sign-in method.
3. **Install dependencies:**
   ```bash
   ./gradlew build
   ```
4. **Run the app:**
   - Use Android Studio’s device manager or a real device.

## Tech Stack

- [Kotlin](https://kotlinlang.org/)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/)
- [Firebase Auth](https://firebase.google.com/docs/auth)
- [Koin](https://insert-koin.io/) (dependency injection)
- [Material Components](https://material.io/develop/android)

## Platform Support

- **Android**: Fully supported

## Project Structure

```
app/
  ├── src/
  │   ├── main/
  │   │   ├── java/org/gabrieal/simplefirebaseauth/
  │   │   │   ├── data/
  │   │   │   │   └── di/    # Koin, DI
  │   │   │   ├── feature/auth/
  │   │   │   │   ├── view/         # Activities, UI logic
  │   │   │   │   └── viewmodel/    # ViewModels, State
  │   │   │   └── helper/           # Utility functions
  │   │   └── res/                  # Layouts, drawables, values
  ├── build.gradle
  └── ...
```

---
