# TracTrail

TracTrail is an Android app for tracking daily habits, workouts, meals/nutrition, and
personal analytics — all in one place. Built with Kotlin and Jetpack Compose.

## Features

- **Habits** — Create and track daily habits (Health, Fitness, Productivity, and more),
  log progress against goals (e.g. glasses of water, pages read), and view active vs.
  archived habits.
- **Workouts** — Log workouts by type (Strength Training, Cardio, HIIT, etc.), track
  active time, calories burned, and exercises completed per session, with notes for
  each entry.
- **Meals** — Log meals by category (Breakfast, Lunch, Dinner, Snack) with calories and
  macro breakdown (protein, carbs, fat), and track daily calorie goals against intake.
- **Analytics** — View a consistency index, best streaks, total habit logs, workout
  time, and calories burned, plus a 7-day activity chart, over Week/Month/All-Time
  ranges.
- A shared date-strip navigator (day-by-day) and light/dark theme toggle across all
  screens.

## Tech Stack

- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose
- **Build System:** Gradle (Kotlin DSL — `.gradle.kts`)
- **Annotation Processing:** KSP (Kotlin Symbol Processing)
- **Dependency Management:** Gradle version catalogs (`libs.versions.toml`)

## Project Structure

```
TracTrail/
├── app/                 # Main application module
├── gradle/              # Gradle wrapper and version catalog
├── build.gradle.kts     # Top-level build configuration
├── settings.gradle.kts  # Project/module settings
└── gradlew / gradlew.bat
```

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable release recommended)
- JDK 17 or newer
- An Android emulator or physical device running a supported Android version

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/nirbhay-11/TracTrail.git
   cd TracTrail
   ```
2. Open the project in Android Studio, or build from the command line:
   ```bash
   ./gradlew build
   ```
3. Run the app on an emulator or connected device:
   ```bash
   ./gradlew installDebug
   ```

## Building

- Debug build: `./gradlew assembleDebug`
- Release build: `./gradlew assembleRelease`
- Run tests: `./gradlew test`

## Contributing

Contributions, issues, and feature requests are welcome. Feel free to open an
issue or submit a pull request.

## License

No license has been specified for this project yet. Consider adding one
(e.g., MIT, Apache 2.0) to clarify how others may use this code.
