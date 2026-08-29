# Repository Guidelines

## Project Structure & Module Organization

This is a single-module Android application. Root Gradle configuration lives in `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`, and `gradle/libs.versions.toml`. App code is under `app/src/main/java/com/example`, organized by responsibility: `data` for Room persistence and repositories, `model` for entities/settings, `tts` for text-to-speech integration, `viewmodel` for state, and `ui` for Compose screens, components, and theme. Android resources are in `app/src/main/res`. Unit and Robolectric tests are in `app/src/test/java`; instrumented Android tests are in `app/src/androidTest/java`. Static project assets are in `assets`.

## Build, Test, and Development Commands

This repo currently includes Gradle wrapper metadata but no `gradlew` script, so use a local Gradle installation:

- `gradle :app:assembleDebug` builds a debug APK.
- `gradle :app:assembleRelease` builds a release APK using the configured signing environment.
- `gradle :app:testDebugUnitTest` runs local JVM tests, including Robolectric tests.
- `gradle :app:connectedDebugAndroidTest` runs instrumented tests on a connected emulator or device.
- `gradle :app:check` runs the module verification tasks available through Gradle.

## Coding Style & Naming Conventions

Use Kotlin with Jetpack Compose conventions. Keep indentation at two spaces, matching the existing Gradle and Kotlin files. Name composables with PascalCase nouns or noun phrases, such as `ReaderScreen` and `ArticleCard`. Keep stateful logic in `PocketViewModel` or related model/data classes rather than inside large composables. Use package paths under `com.example` until the application namespace is intentionally renamed.

## Testing Guidelines

Use JUnit for simple unit tests, Robolectric for Android framework behavior, Compose UI test APIs for UI behavior, and Roborazzi for screenshot coverage. Place local tests in `app/src/test/java/com/example` and instrumented tests in `app/src/androidTest/java/com/example`. Prefer descriptive test class names ending in `Test`, `RobolectricTest`, or `ScreenshotTest`.

## Commit & Pull Request Guidelines

Recent history uses short Conventional Commit-style messages, for example `feat: initialize Android application project` and `ci: automate APK releases to GitHub`. Continue using `<type>: <imperative summary>` with types such as `feat`, `fix`, `test`, `refactor`, and `ci`.

Pull requests should describe the user-visible change, list verification commands run, link related issues when applicable, and include screenshots or recordings for UI changes. Note any configuration or signing requirements, especially changes involving `.env`, Firebase, or release keystores.

## Security & Configuration Tips

Do not commit secrets, keystores, generated APKs, or local `.env` values. Use `.env.example` for documented configuration names. Release signing reads `KEYSTORE_PATH`, `STORE_PASSWORD`, and `KEY_PASSWORD`; keep those values in local or CI secrets only.
