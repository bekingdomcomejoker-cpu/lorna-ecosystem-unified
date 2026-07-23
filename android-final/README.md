# LORNA v3 - Android Application

Local Offline Reasoning Node Architecture for Android

## Features

- **Native Android App** - Jetpack Compose UI
- **Local LLM Inference** - llama.cpp integration
- **Four-Point Linguistic Lens** - Phonetic, Semantic, Morphological, Symbolic analysis
- **Memory Spine** - Cross-model memory persistence
- **Streaming Chat** - Real-time token streaming
- **Model Management** - Load/unload models dynamically
- **RAG Integration** - On-device retrieval augmented generation
- **Speech I/O** - Voice input and text-to-speech output

## Project Structure

```
LORNA-Android/
├── app/
│   ├── src/main/
│   │   ├── java/com/lorna/
│   │   │   ├── MainActivity.kt
│   │   │   ├── ui/
│   │   │   │   ├── screens/
│   │   │   │   │   └── ChatScreen.kt
│   │   │   │   ├── components/
│   │   │   │   │   └── Components.kt
│   │   │   │   └── theme/
│   │   │   │       ├── Theme.kt
│   │   │   │       └── Type.kt
│   │   │   ├── data/
│   │   │   │   ├── db/
│   │   │   │   └── repository/
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   └── usecase/
│   │   │   └── utils/
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Requirements

- Android SDK 24+
- Kotlin 1.9.21+
- Gradle 8.2.0+

## Build

```bash
./gradlew build
./gradlew assembleRelease
```

## Install

```bash
adb install app/build/outputs/apk/release/app-release.apk
```

## Architecture

### UI Layer (Jetpack Compose)
- ChatScreen - Main chat interface
- MetricsPanel - Real-time telemetry
- Components - Reusable UI elements

### Data Layer
- Room Database - Local persistence
- Repository Pattern - Data access

### Domain Layer
- Models - Business logic entities
- Use Cases - Business logic operations

### Integration
- llama.cpp JNI bridge
- FAISS for RAG
- Speech Recognition API

## Configuration

Edit `app/build.gradle.kts` to customize:
- Min SDK version
- Target SDK version
- Dependencies

## Performance (Redmi 13C)

- DeepSeek R1 1.5B: 5.0 t/s
- Llama 3.2 1B: 6.8 t/s
- Qwen 0.5B: 15.0 t/s

## License

Proprietary - LORNA Project
