# Seekho-Anime-Assignment
Pocket Otaku: A modern Android application built with MVVM, Clean Architecture, and Room to fetch and cache top anime data using the Jikan API. Developed for the Seekho Android Developer Assignment.

## 📺 YouTube Player Implementation & Challenges

Implementing a stable YouTube player inside the app presented several challenges, specifically handling the YouTube IFrame API restrictions and Android lifecycle events.

### 🔴 The Issues
1.  **Manual WebView Limitations**: Our initial implementation using a standard `WebView` was prone to "Error 153" (restricted playback) and "Error 152" (content requires specific player configuration). It also struggled with lifecycle management, causing videos to keep playing in the background or reload unnecessarily.
2.  **API Restrictions**: Simple embed URLs often failed validation strings or were rejected by YouTube's mobile web player logic.
3.  **Race Conditions**: Even after switching to a library, we faced race conditions where the player would attempt to load before initialization was complete, leading to black screens or crashes.

### 🟢 The Solution
We resolved these issues by adopting the **Android-YouTube-Player** library and applying robust configuration patterns derived from the following resources:

#### 1. [Compose Meets YouTube: Production-Ready YouTube Playback](https://proandroiddev.com/compose-meets-youtube-production-ready-youtube-playback-with-jetpack-compose-9e55013b411a)
*   **What we learned**: Recommended replacing manual WebViews with the `PierfrancescoSoffritti/android-youtube-player` library.
*   **Implementation**: This provided the base `YouTubePlayerView` wrapper, handling the complex JavaScript bridging and basic lifecycle events automatically.

#### 2. [Fixing YouTube Playback: The 3 Changes That Brought My Player Back](https://proandroiddev.com/fixing-youtube-playback-in-jetpack-compose-the-3-changes-that-brought-my-player-back-cd0adcff3766)
*   **What we learned**: To fix the persistent Error 153/152 and "unavailable video" states, we needed to take manual control of the player initialization.
*   **Implementation**:
    *   **Disabled Auto-Init**: set `enableAutomaticInitialization = false` to prevent the library from loading too early.
    *   **IFramePlayerOptions**: Manually configured the player with strict options (`controls=1`, `rel=0`, `iv_load_policy=3`) to ensure a clean IFrame state.
    *   **Robust ID Extraction**: Refactored our URL parsing logic to reliably extract Video IDs from any YouTube URL format (Shorts, Watch, Embed), ensuring the player always receives a valid ID.
