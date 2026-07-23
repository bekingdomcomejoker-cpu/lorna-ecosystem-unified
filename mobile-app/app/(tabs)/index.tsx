import React, { useState, useEffect, useRef } from "react";
import {
  ScrollView,
  Text,
  View,
  TextInput,
  Pressable,
  ActivityIndicator,
  FlatList,
  KeyboardAvoidingView,
  Platform,
  Alert,
} from "react-native";
import AsyncStorage from "@react-native-async-storage/async-storage";
import * as Haptics from "expo-haptics";
import { ScreenContainer } from "@/components/screen-container";
import { ConversationTopics } from "@/components/conversation-topics";
import { VoiceInput } from "@/components/voice-input";
import { useColors } from "@/hooks/use-colors";

interface Message {
  id: string;
  text: string;
  sender: "user" | "ai";
  timestamp: Date;
}

const STORAGE_KEY = "lorna_messages";
const API_KEY_STORAGE = "lorna_gemini_api_key";
const GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";

export default function ChatScreen() {
  const colors = useColors();
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputText, setInputText] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [apiKey, setApiKey] = useState<string | null>(null);
  const [showVoiceInput, setShowVoiceInput] = useState(false);
  const scrollViewRef = useRef<ScrollView>(null);

  // Load messages and API key on mount
  useEffect(() => {
    loadMessages();
    loadApiKey();
  }, []);

  // Scroll to bottom when new messages arrive
  useEffect(() => {
    if (messages.length > 0) {
      setTimeout(() => {
        scrollViewRef.current?.scrollToEnd({ animated: true });
      }, 100);
    }
  }, [messages]);

  const loadMessages = async () => {
    try {
      const stored = await AsyncStorage.getItem(STORAGE_KEY);
      if (stored) {
        const parsed = JSON.parse(stored);
        setMessages(
          parsed.map((msg: any) => ({
            ...msg,
            timestamp: new Date(msg.timestamp),
          }))
        );
      }
    } catch (error) {
      console.error("Failed to load messages:", error);
    }
  };

  const loadApiKey = async () => {
    try {
      const key = await AsyncStorage.getItem(API_KEY_STORAGE);
      setApiKey(key);
    } catch (error) {
      console.error("Failed to load API key:", error);
    }
  };

  const saveMessages = async (msgs: Message[]) => {
    try {
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(msgs));
    } catch (error) {
      console.error("Failed to save messages:", error);
    }
  };

  const callGeminiAPI = async (userMessage: string): Promise<string> => {
    if (!apiKey) {
      throw new Error("API key not configured. Please set it in settings.");
    }

    const response = await fetch(`${GEMINI_API_URL}?key=${apiKey}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        contents: [
          {
            parts: [
              {
                text: userMessage,
              },
            ],
          },
        ],
      }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error?.message || "Failed to get response from Gemini API");
    }

    const data = await response.json();
    return data.candidates[0].content.parts[0].text;
  };

  const handleTopicSelect = (topic: any) => {
    setInputText(`Tell me about ${topic.title.toLowerCase()}: ${topic.description}`);
  };

  const handleVoiceTranscription = (text: string) => {
    setInputText(text);
    setShowVoiceInput(false);
  };

  const handleSendMessage = async () => {
    if (!inputText.trim()) return;

    if (!apiKey) {
      Alert.alert("API Key Required", "Please configure your Gemini API key in settings.");
      return;
    }

    const userMessage = inputText.trim();
    setInputText("");

    // Add user message
    const newUserMessage: Message = {
      id: Date.now().toString(),
      text: userMessage,
      sender: "user",
      timestamp: new Date(),
    };

    const updatedMessages = [...messages, newUserMessage];
    setMessages(updatedMessages);
    await saveMessages(updatedMessages);

    // Get AI response
    setIsLoading(true);
    try {
      const aiResponse = await callGeminiAPI(userMessage);
      const aiMessage: Message = {
        id: (Date.now() + 1).toString(),
        text: aiResponse,
        sender: "ai",
        timestamp: new Date(),
      };

      const finalMessages = [...updatedMessages, aiMessage];
      setMessages(finalMessages);
      await saveMessages(finalMessages);

      // Haptic feedback
      Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
    } catch (error) {
      console.error("Error calling Gemini API:", error);
      Alert.alert(
        "Error",
        error instanceof Error ? error.message : "Failed to get response from AI"
      );
      // Remove the user message if API call failed
      setMessages(messages);
      await saveMessages(messages);
    } finally {
      setIsLoading(false);
    }
  };

  const renderMessage = ({ item }: { item: Message }) => {
    const isUser = item.sender === "user";
    return (
      <View
        className={`flex-row mb-3 ${isUser ? "justify-end" : "justify-start"}`}
      >
        <View
          className={`max-w-xs rounded-2xl px-4 py-3 ${
            isUser
              ? "bg-primary rounded-br-none"
              : "bg-surface rounded-bl-none"
          }`}
        >
          <Text
            className={`text-base leading-relaxed ${
              isUser ? "text-background" : "text-foreground"
            }`}
          >
            {item.text}
          </Text>
          <Text
            className={`text-xs mt-1 ${
              isUser ? "text-background opacity-70" : "text-muted"
            }`}
          >
            {item.timestamp.toLocaleTimeString([], {
              hour: "2-digit",
              minute: "2-digit",
            })}
          </Text>
        </View>
      </View>
    );
  };

  return (
    <ScreenContainer
      className="flex-1 bg-background"
      containerClassName="bg-background"
    >
      <KeyboardAvoidingView
        behavior={Platform.OS === "ios" ? "padding" : "height"}
        className="flex-1"
      >
        <View className="flex-1">
          {/* Header */}
          <View className="px-4 py-4 border-b border-border">
            <Text className="text-2xl font-bold text-foreground">LORNA</Text>
            <Text className="text-sm text-muted">AI-Powered Chat</Text>
          </View>

          {/* Messages List */}
          {messages.length === 0 ? (
            <View className="flex-1 items-center justify-center px-6">
              <Text className="text-lg font-semibold text-foreground mb-2">
                Welcome to LORNA
              </Text>
              <Text className="text-center text-muted">
                Start a conversation with your AI assistant. Type a message below to begin.
              </Text>
            </View>
          ) : (
            <FlatList
              ref={scrollViewRef as any}
              data={messages}
              renderItem={renderMessage}
              keyExtractor={(item) => item.id}
              contentContainerStyle={{ padding: 16, paddingBottom: 8 }}
              scrollEnabled={true}
            />
          )}

          {/* Loading Indicator */}
          {isLoading && (
            <View className="px-4 py-3 flex-row items-center gap-2">
              <ActivityIndicator
                size="small"
                color={colors.primary}
              />
              <Text className="text-sm text-muted">LORNA is thinking...</Text>
            </View>
          )}

          {/* Input Area */}
          <View className="px-4 py-4 border-t border-border bg-background">
            <View className="flex-row items-end gap-2">
              <TextInput
                value={inputText}
                onChangeText={setInputText}
                placeholder="Type your message..."
                placeholderTextColor={colors.muted}
                multiline
                maxLength={500}
                className="flex-1 bg-surface text-foreground px-4 py-3 rounded-2xl"
                editable={!isLoading}
              />
              <Pressable
                onPress={() => setShowVoiceInput(true)}
                disabled={isLoading}
                style={({ pressed }) => [
                  {
                    opacity: pressed ? 0.7 : 1,
                    transform: [{ scale: pressed ? 0.95 : 1 }],
                  },
                ]}
                onPressIn={() => {
                  if (!isLoading) {
                    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
                  }
                }}
              >
                <View className="bg-surface w-12 h-12 rounded-full items-center justify-center border border-border">
                  <Text className="text-xl">🎤</Text>
                </View>
              </Pressable>
              <Pressable
                onPress={handleSendMessage}
                disabled={isLoading || !inputText.trim()}
                style={({ pressed }) => [
                  {
                    opacity: pressed ? 0.7 : 1,
                    transform: [{ scale: pressed ? 0.95 : 1 }],
                  },
                ]}
                onPressIn={() => {
                  if (!isLoading && inputText.trim()) {
                    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
                  }
                }}
              >
                <View className="bg-primary w-12 h-12 rounded-full items-center justify-center">
                  <Text className="text-xl text-background font-bold">→</Text>
                </View>
              </Pressable>
            </View>
          </View>
        </View>
      </KeyboardAvoidingView>

      {/* Voice Input Modal */}
      <VoiceInput
        isVisible={showVoiceInput}
        onClose={() => setShowVoiceInput(false)}
        onTranscriptionComplete={handleVoiceTranscription}
      />
    </ScreenContainer>
  );
}
