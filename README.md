# ai-voice-chat-app
Kotlin - Android Studio


## App Features

- Real-time voice chat: Streaming speech recognition (STT) with synthesized assistant replies (TTS).

- Tap-to-speak toggle: Tap any message to start/stop playback instantly.

- Multi-language support: English, Turkish, French, German — UI strings and TTS locale update immediately after selection.

- Settings in Jetpack Compose: Material 3 screen with TopAppBar and an exposed dropdown language picker (flag + title), state persisted via rememberSaveable, seamless back navigation.

- Conversation feed: Welcome message on first run and continuous chat list with auto-scroll.

- Permission UX: Microphone permission flow with settings deep-link and graceful fallbacks.

- Operational controls: Remote configuration flags (enable/disable API usage, change API key remotely) and resilient error handling with retry/backoff.

- Observability: Crash reporting and lightweight analytics.


## Technologies Used

- Language & Concurrency: Kotlin, Coroutines, Flow/StateFlow.

- UI (Compose): Jetpack Compose (Material 3), Scaffold/TopAppBar, ExposedDropdownMenuBox, TextField with trailing flag icon, remember / rememberSaveable for state, ComposeView interop inside Fragment.

- Architecture: MVVM, Repository pattern, AndroidX Navigation, Data Binding.

- Speech: Android SpeechRecognizer (STT), TextToSpeech (TTS).

- Networking: Retrofit 2, OkHttp 3, Gson converter, HttpLoggingInterceptor.

- Firebase: Remote Config, Analytics, Crashlytics (via BOM).

- UI: Material Components, RecyclerView.

- Build/Tooling: Gradle, Android Gradle Plugin, Compose BOM, Kotlin Compose Compiler plugin (Kotlin 2.x).



<img width="512" height="512" alt="ic_launcher_playstore" src="https://github.com/user-attachments/assets/de7d5bc7-9236-4613-8ddf-0b93e3b29e0b" />

<img width="421" height="885" alt="Screenshot_1" src="https://github.com/user-attachments/assets/1161b260-c0d7-4f8f-bdd7-6f2e22bfc6dc" />

<img width="424" height="882" alt="Screenshot_2" src="https://github.com/user-attachments/assets/91a0e09b-f3d2-46a4-aa90-015a09409747" />

<img width="419" height="878" alt="Screenshot_3" src="https://github.com/user-attachments/assets/0b768fb2-137d-4778-bf83-f59da58f01b2" />
