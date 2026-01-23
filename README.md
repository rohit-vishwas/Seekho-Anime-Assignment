# Pocket Otaku - Anime Discovery App

**Pocket Otaku** is a modern Android application built to help users discover top-rated anime. It utilizes the **Jikan API** (Unofficial MyAnimeList API) to fetch data and offers a seamless offline experience using **Room Database**.

---

## 📌 Features Implemented

### 1. 🏠 Home Screen (Anime List)
*   **Top Anime Fetch**: Retrieves a list of top-ranked anime from Jikan V4 API.
*   **Offline Support**: Automatically caches fetched data into a local Room database. If the network is unavailable, the app serves data from the database.
*   **Pull-to-Refresh**: Allows users to force a data refresh from the server.
*   **Grid Layout**: Displays anime covers, titles, and ratings in an attractive, responsive grid.
*   **Error Handling**: Gracefully handles network errors and empty states with informative toasts and UI prompts.

### 2. 🎬 Detail Screen (Immersive Experience)
*   **Parallax Header**: Features a fixed video player (or high-res poster) at the top that stays visible while scrolling.
*   **Overlapping Content Sheet**: A modern "bottom sheet" style layout for details (Title, Synopsis, Genres) that scrolls over the header.
*   **Interactive Video Player**: Embedded YouTube player for trailers, fully interactive even when the content sheet overlaps the bottom edge.
*   **Rich Metadata**: Displays release year, episode count, status, and genres in a clean, chip-based layout.

### 3. 🏗 Design & Architecture
*   **MVVM Architecture**: Clean separation of concerns using ViewModel and Repository pattern.
*   **Dependency Injection**: Powered by **Koin** (migrated from Hilt) for lightweight and Kotlin-friendly DI.
*   **Jetpack Compose**: 100% Kotlin-based UI toolkit for reactive and modern UI development.
*   **Material 3**: Follows the latest Material Design guidelines.

---

## 📋 Assumptions Made

1.  **API Rate Limits**: The Jikan API has rate limits. The app currently handles basic fetching, but heavy usage might trigger API throttling (429 errors). We assume standard usage patterns.
2.  **Device Compatibility**: The app is designed for devices running **Android 7.0 (API 24)** and above.
3.  **Data Persistence**: Users expect to see previously loaded content when offline. We assume the last successful fetch is sufficient for offline viewing.
4.  **Trailer Availability**: Not all anime have trailers. The UI falls back gracefully to the poster image if no video URL is provided.

---

## ⚠️ Known Limitations

1.  **Pagination**: Currently, the app fetches the first page of "Top Anime". Infinite scrolling/pagination is not yet implemented.
2.  **Background Sync**: There is no background work (WorkManager) to sync data when the app is closed. Sync occurs only on app launch or manual refresh.

---

## 🛠 Tech Stack

*   **Language**: Kotlin
*   **UI**: Jetpack Compose, Material3
*   **Architecture**: MVVM
*   **Dependency Injection**: Koin
*   **Networking**: Retrofit, OkHttp, Gson
*   **Database**: Room (SQLite)
*   **Image Loading**: Coil
*   **Async**: Coroutines, Flow
*   **Video**: Android-YouTube-Player

---

## 📺 Technical Deep Dive: YouTube Player Implementation

Implementing a stable YouTube player inside a Compose scrollable layout presented unique challenges.

### 🔴 The Issues
1.  **Touch Interception**: Placing a video player behind a scrolling list (for a parallax effect) initially blocked touch events to the player controls.
2.  **Lifecycle Management**: Standard WebViews often leaked audio or failed to stop playback when navigating away.
3.  **Initialization Crashes**: Manual initialization logic sometimes caused race conditions leading to black screens.

### 🟢 The Solution
*   **Layout Strategy**: We used a **Column-based layout** where the Video Player is a **fixed sibling** to the scrollable content surface.
    *   The scrollable surface has a **custom layout modifier** that increases its height by `24dp` and shifts it upwards. This creates the visual effect of the content "overlapping" the video without physically covering the video's touch processing area.
*   **Library**: We adopted `PierfrancescoSoffritti/android-youtube-player` for robust lifecycle handling.
*   **Configuration**: We disabled automatic initialization and manually configured `IFramePlayerOptions` to ensure reliable loading of video IDs extracted from various URL formats.

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
