# LORNA Mobile App - Interface Design

## Overview

LORNA is an AI-powered mobile chat application featuring real-time conversations with Google's Gemini API. The app emphasizes a clean, iOS-style interface optimized for one-handed usage in portrait orientation.

## Screen List

1. **Chat Screen** (Home) - Main conversation interface
2. **Settings Screen** - Theme toggle, API key management, chat history
3. **Search Screen** - Find past conversations and messages
4. **Voice Input Modal** - Hands-free message composition

## Screen Details

### 1. Chat Screen (Home)
**Purpose:** Primary interface for user-AI conversations

**Content & Functionality:**
- Message list displaying conversation history (user messages on right, AI responses on left)
- Each message shows timestamp and sender identification
- Scrollable message area for long conversations
- Text input field at bottom with send button
- Voice input button (microphone icon) next to send
- Loading indicator when AI is processing response
- Clear chat history button (accessible via settings)

**Layout:**
- Header: App title "LORNA" with settings icon (top-right)
- Message area: Full-width scrollable list with proper padding
- Input area: Fixed at bottom with safe area inset handling
- Haptic feedback on send button press

### 2. Settings Screen
**Purpose:** Configuration and app management

**Content & Functionality:**
- Theme toggle (Light/Dark mode)
- Chat history management (view count, clear all option)
- API key status indicator
- App information and version
- Privacy/terms links

**Layout:**
- List-style sections with toggle switches
- Clear visual hierarchy with section headers
- Confirmation dialog before clearing chat history

### 3. Search Screen
**Purpose:** Find and filter past conversations

**Content & Functionality:**
- Search input field at top
- Filtered message results with context snippets
- Tap to jump to message in chat
- Filter by date range (optional)
- No results state with helpful message

**Layout:**
- Search bar with clear button
- Results list with message preview and timestamp
- Empty state illustration

### 4. Voice Input Modal
**Purpose:** Hands-free message composition

**Content & Functionality:**
- Large microphone icon (recording indicator)
- Real-time transcription display
- Cancel and Send buttons
- Error handling for permission issues

**Layout:**
- Modal overlay with centered content
- Large, easy-to-tap controls
- Visual feedback during recording

## Key User Flows

### Flow 1: Send a Text Message
1. User taps chat input field
2. Types message using keyboard
3. Taps send button
4. Message appears immediately in chat (user side)
5. Loading indicator shows while AI processes
6. AI response appears in chat
7. Haptic feedback on send

### Flow 2: Send a Voice Message
1. User taps microphone button
2. Voice input modal appears
3. User speaks (real-time transcription shown)
4. User taps send button
5. Transcribed text is sent as message
6. Rest of flow same as text message

### Flow 3: Search Past Conversations
1. User taps search tab
2. Types search query
3. Results appear with message snippets
4. User taps result to jump to that message in chat
5. Chat screen opens with message highlighted

### Flow 4: Toggle Dark Mode
1. User navigates to settings
2. Taps theme toggle
3. App immediately switches to dark/light mode
4. Setting persists across app restarts

## Color Choices

### Light Mode
- **Background:** #FFFFFF (white)
- **Surface:** #F5F5F5 (light gray)
- **Foreground:** #11181C (dark text)
- **Primary:** #0A7EA4 (teal accent)
- **Muted:** #687076 (secondary text)
- **Border:** #E5E7EB (light dividers)

### Dark Mode
- **Background:** #151718 (near-black)
- **Surface:** #1E2022 (dark gray)
- **Foreground:** #ECEDEE (light text)
- **Primary:** #0A7EA4 (teal accent - consistent)
- **Muted:** #9BA1A6 (secondary text)
- **Border:** #334155 (dark dividers)

## Interaction Patterns

- **Primary buttons:** Scale 0.97 on press + light haptic feedback
- **List items:** Opacity 0.7 on press
- **Icons:** Opacity 0.6 on press
- **Loading states:** Spinner with "Thinking..." text
- **Error states:** Red banner with retry option

## Accessibility

- Minimum touch target size: 44x44 points
- High contrast ratios for text (WCAG AA compliant)
- Voice input as alternative to typing
- Clear visual hierarchy with consistent spacing
- Haptic feedback for important interactions
