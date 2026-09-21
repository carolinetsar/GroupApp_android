# MEMORY.md — GroupApp

## Project conventions

- **Project root:** `workbuddy-ai/GroupApp` (Android Studio project, Gradle Kotlin DSL).
- **Package root:** `com.groupapp`. Layout: `data/` (models + repository), `ui/` (nav, ViewModel),
  `ui/screens/`, `ui/components/`, `ui/theme/`, `util/`.
- **UI language is Russian.** All user-facing strings are hardcoded in the composables
  (no `strings.xml` extraction yet — deliberate for this stage).
- **Toolchain is pinned deliberately** and works together; do not bump one piece alone:
  AGP 8.5.2 / Gradle 8.7 / Kotlin 1.9.24 / Compose compiler 1.5.14 / Compose BOM 2024.06.00.
- **Icons:** only `material-icons-core` is on the classpath (via material3). Do NOT use icons from
  `material-icons-extended` without adding that dependency.
- **No Google Play Services.** Geolocation uses the platform `LocationManager` + nearest-city
  lookup in `Geo.kt`.
- **Persistence pattern:** one `AppData` object serialized to SharedPreferences as JSON.
  Mutations go through `GatheringRepository.update { }`, which writes through on every change.

## Environment

- This machine has **no JDK, no Gradle, no Android SDK** — Android builds cannot be run or verified
  locally. Validate changes by careful static review instead.
- The Gradle wrapper JAR is a binary; fetch it with curl rather than writing it as text.
