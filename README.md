# ai-voice-chat-app
Kotlin - Android Studio


## App Features

- Real-time voice chat: Streaming speech recognition (STT) with synthesized assistant replies (TTS).

- Tap-to-speak toggle: Tap any message to start/stop playback instantly.

- Multi-language support: English, Turkish, French, German — UI strings and TTS locale update immediately after selection.

- Conversation feed: Welcome message on first run and continuous chat list with auto-scroll.

- Permission UX: Microphone permission flow with settings deep-link and graceful fallbacks.

- Operational controls: Remote configuration flags (enable/disable API usage) and resilient error handling with retry/backoff.

- Observability: Crash reporting and lightweight analytics.


Technologies Used

- Language & Concurrency: Kotlin, Coroutines, Flow/StateFlow.

- Architecture: MVVM, Repository pattern, AndroidX Navigation, Data Binding.

- Speech: Android SpeechRecognizer (STT), TextToSpeech (TTS).

- Networking: Retrofit 2, OkHttp 3, Gson converter, HttpLoggingInterceptor.

- Firebase: Remote Config, Analytics, Crashlytics (via BOM).

- UI: Material Components, RecyclerView.

- Build/Tooling: Gradle, Android Gradle Plugin.


<img width="512" height="512" alt="ic_launcher_playstore" src="https://github.com/user-attachments/assets/de7d5bc7-9236-4613-8ddf-0b93e3b29e0b" />
