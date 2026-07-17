# MiraMAL

**MiraMAL** is an open-source Android client for [MyAnimeList](https://myanimelist.net) built with Kotlin and Jetpack Compose. Browse, search, and track your anime seamlessly with a modern Material 3 interface.

## Features

- **OAuth 2.0 PKCE Login** — Secure authentication via MyAnimeList with automatic token refresh
- **Anime Search** — Search with real-time results and infinite scroll pagination
- **Anime Detail** — Score, rank, popularity, synopsis, genres, studios, season
- **Status Tracking** — Update watching status (Watching, Completed, On Hold, Dropped, Plan to Watch)
- **Favorites** — Local favorites via Room database + sync with your MAL list
- **Dark & Light Theme** — Full Material 3 theming support
- **Edge-to-Edge** — Modern Android edge-to-edge display

## Tech Stack

| Layer | Technology |
|---|---|
| Language | [Kotlin](https://kotlinlang.org/) 2.0 |
| UI | [Jetpack Compose](https://developer.android.com/compose) (Material 3) |
| Architecture | Clean Architecture + MVVM |
| DI | [Dagger Hilt](https://dagger.dev/hilt/) 2.51.1 |
| Networking | [Retrofit](https://square.github.io/retrofit/) 2.11 + [OkHttp](https://square.github.io/okhttp/) 4.12 |
| Serialization | [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) 1.7.1 |
| Database | [Room](https://developer.android.com/training/data-storage/room) 2.6.1 |
| Image Loading | [Coil](https://coil-kt.github.io/coil/) 2.7.0 (Compose) |
| Navigation | [Navigation Compose](https://developer.android.com/guide/navigation/compose) 2.7.7 |
| Auth Storage | [EncryptedSharedPreferences](https://developer.android.com/topic/security/data) (AES-256) |
| Preferences | [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore) 1.1.1 |
| Annotation Processing | [KSP](https://github.com/google/ksp) (Kotlin Symbol Processing) |
| Build | [Gradle](https://gradle.org/) 9.6.0 + Kotlin DSL + Version Catalog |

## Architecture

The project follows **Clean Architecture** with 3 layers and **MVVM** in the presentation layer.

```
┌────────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                       │
│  Compose Screens → ViewModels (StateFlow<UiState>)         │
│  Navigation (NavHost + bottom nav)                         │
├────────────────────────────────────────────────────────────┤
│                     DOMAIN LAYER                           │
│  Domain models, Repository interfaces, Use cases           │
│  (pure Kotlin, no Android dependencies)                    │
├────────────────────────────────────────────────────────────┤
│                      DATA LAYER                            │
│  Retrofit APIs / DTOs → Mappers → Room DB / Auth Store     │
│  Repository implementations                                │
├────────────────────────────────────────────────────────────┤
│                   DI LAYER (Hilt Modules)                  │
│  AppModule, NetworkModule, AuthModule, RepositoryModule    │
└────────────────────────────────────────────────────────────┘
```

### Project Structure

```
app/
├── src/main/java/com/szmaou/miramal/
│   ├── MiraMALApp.kt              # @HiltAndroidApp
│   ├── MainActivity.kt            # Single activity, edge-to-edge, auth deep link
│   │
│   ├── di/                        # Hilt DI modules
│   │   ├── AppModule.kt           # Room database provider
│   │   ├── NetworkModule.kt       # Retrofit, OkHttp, API interfaces
│   │   ├── AuthModule.kt          # Client ID & redirect URI providers
│   │   └── RepositoryModule.kt    # Binds repo interfaces → implementations
│   │
│   ├── data/                      # Data layer
│   │   ├── local/                 # Room DB, entity, DAO
│   │   ├── remote/
│   │   │   ├── api/               # MAL API interfaces (anime, user)
│   │   │   ├── auth/              # OAuth 2.0 PKCE (manager, interceptor, store)
│   │   │   └── dto/               # API response/request DTOs
│   │   ├── mapper/                # DTO ↔ Domain ↔ Entity converters
│   │   └── repository/            # Repository implementations
│   │
│   ├── domain/                    # Domain layer
│   │   ├── model/                 # Anime, MyListStatus, UserAnime
│   │   ├── repository/            # Repository interfaces
│   │   └── usecase/               # SearchAnime, GetAnimeDetail, ManageFavorite
│   │
│   └── presentation/             # Presentation layer
│       ├── auth/                  # Login screen & ViewModel
│       ├── search/                # Search screen & ViewModel (pagination)
│       ├── detail/                # Detail screen & ViewModel
│       ├── favorite/              # Favorites (local + MAL list) screen & VM
│       ├── navigation/            # NavGraph, Screen sealed class
│       ├── components/            # AnimeCard, SearchBar, LoadingIndicator
│       └── theme/                 # Color, Type, Theme (light/dark)
│
├── src/main/res/                  # Resources (drawables, themes, mipmaps)
└── build.gradle.kts               # App build config
```

## Prerequisites

- JDK 17+
- Android Studio (Electric Eel or later recommended)
- Android SDK (API 34)
- A physical device or emulator running Android 8.0+

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/MiraMAL.git
cd MiraMAL
```

### 2. Configure API credentials

MiraMAL uses the MyAnimeList API. You need to provide your own Client ID. Create or edit `local.properties`:

```properties
sdk.dir=/path/to/Android/Sdk
MAL_CLIENT_ID=your_client_id_here
```

> [!NOTE]
> `local.properties` is gitignored, so your credentials stay local. The Client ID is injected as `BuildConfig.MAL_CLIENT_ID` at build time.

### 3. Build & Run

```bash
# Build debug APK
./gradlew assembleDebug

# Build and install on connected device
./gradlew installDebug

# Build release APK
./gradlew assembleRelease
```

### Getting a MyAnimeList Client ID

1. Go to [MyAnimeList API](https://myanimelist.net/apiconfigrations)
2. Create a new application
3. Set the redirect URI to `miramal://auth`
4. Copy the Client ID into your `local.properties`

## Screenshots

<!-- TODO: Add screenshots -->

## Roadmap

- [x] OAuth 2.0 PKCE authentication with auto token refresh
- [x] Anime search with infinite scroll pagination
- [x] Anime detail & MAL status tracking
- [x] Local favorites (Room database)
- [x] MAL anime list sync
- [ ] Unit, integration & UI tests
- [ ] Seasonal anime charts
- [ ] Anime recommendations
- [ ] Push notifications
- [ ] CI/CD pipeline

## License

```
Copyright 2026 MiraMAL

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Contributing

Contributions are welcome! Feel free to open an issue or submit a pull request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Disclaimer

MiraMAL is an unofficial client for MyAnimeList and is not affiliated with or endorsed by MyAnimeList.
