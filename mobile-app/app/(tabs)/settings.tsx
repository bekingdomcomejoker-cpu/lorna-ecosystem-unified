import React, { useState, useEffect } from "react";
import {
  ScrollView,
  Text,
  View,
  Pressable,
  TextInput,
  Alert,
  Switch,
} from "react-native";
import AsyncStorage from "@react-native-async-storage/async-storage";
import * as Haptics from "expo-haptics";
import { ScreenContainer } from "@/components/screen-container";
import { useColors } from "@/hooks/use-colors";
import { useColorScheme } from "@/hooks/use-color-scheme";
import { useThemeContext } from "@/lib/theme-provider";

const API_KEY_STORAGE = "lorna_gemini_api_key";
const STORAGE_KEY = "lorna_messages";

export default function SettingsScreen() {
  const colors = useColors();
  const { colorScheme, setColorScheme } = useThemeContext();
  const [apiKey, setApiKey] = useState("");
  const [messageCount, setMessageCount] = useState(0);
  const [showApiInput, setShowApiInput] = useState(false);
  const [tempApiKey, setTempApiKey] = useState("");

  useEffect(() => {
    loadSettings();
  }, []);

  const loadSettings = async () => {
    try {
      const key = await AsyncStorage.getItem(API_KEY_STORAGE);
      if (key) setApiKey(key);

      const messages = await AsyncStorage.getItem(STORAGE_KEY);
      if (messages) {
        const parsed = JSON.parse(messages);
        setMessageCount(parsed.length);
      }
    } catch (error) {
      console.error("Failed to load settings:", error);
    }
  };

  const handleSaveApiKey = async () => {
    if (!tempApiKey.trim()) {
      Alert.alert("Error", "Please enter a valid API key");
      return;
    }

    try {
      await AsyncStorage.setItem(API_KEY_STORAGE, tempApiKey.trim());
      setApiKey(tempApiKey.trim());
      setShowApiInput(false);
      setTempApiKey("");
      Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
      Alert.alert("Success", "API key saved successfully");
    } catch (error) {
      Alert.alert("Error", "Failed to save API key");
    }
  };

  const handleClearHistory = () => {
    Alert.alert(
      "Clear Chat History",
      "Are you sure you want to delete all messages? This cannot be undone.",
      [
        { text: "Cancel", onPress: () => {} },
        {
          text: "Clear",
          onPress: async () => {
            try {
              await AsyncStorage.removeItem(STORAGE_KEY);
              setMessageCount(0);
              Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
              Alert.alert("Success", "Chat history cleared");
            } catch (error) {
              Alert.alert("Error", "Failed to clear chat history");
            }
          },
          style: "destructive",
        },
      ]
    );
  };

  return (
    <ScreenContainer className="flex-1 bg-background">
      <ScrollView contentContainerStyle={{ paddingBottom: 32 }}>
        {/* Header */}
        <View className="px-6 py-6 border-b border-border">
          <Text className="text-2xl font-bold text-foreground">Settings</Text>
        </View>

        {/* Theme Section */}
        <View className="px-6 py-6 border-b border-border">
          <Text className="text-lg font-semibold text-foreground mb-4">
            Appearance
          </Text>
          <View className="flex-row items-center justify-between bg-surface rounded-lg px-4 py-3">
            <Text className="text-base text-foreground">Dark Mode</Text>
            <Switch
              value={colorScheme === "dark"}
              onValueChange={() => setColorScheme(colorScheme === "dark" ? "light" : "dark")}
              trackColor={{ false: colors.border, true: colors.primary }}
              thumbColor={colors.background}
            />
          </View>
        </View>

        {/* API Configuration Section */}
        <View className="px-6 py-6 border-b border-border">
          <Text className="text-lg font-semibold text-foreground mb-4">
            API Configuration
          </Text>

          {/* API Key Status */}
          <View className="bg-surface rounded-lg px-4 py-3 mb-3">
            <Text className="text-sm text-muted mb-1">Gemini API Key</Text>
            <Text className="text-base text-foreground font-mono">
              {apiKey ? `${apiKey.substring(0, 10)}...` : "Not configured"}
            </Text>
            <Text className="text-xs text-muted mt-2">
              {apiKey ? "✓ Configured" : "⚠ Not configured"}
            </Text>
          </View>

          {/* API Key Input */}
          {showApiInput && (
            <View className="mb-3">
              <TextInput
                value={tempApiKey}
                onChangeText={setTempApiKey}
                placeholder="Paste your Gemini API key here"
                placeholderTextColor={colors.muted}
                secureTextEntry
                multiline
                className="bg-surface text-foreground px-4 py-3 rounded-lg mb-3"
              />
              <View className="flex-row gap-2">
                <Pressable
                  onPress={() => {
                    setShowApiInput(false);
                    setTempApiKey("");
                  }}
                  className="flex-1 bg-surface rounded-lg py-3 items-center"
                  style={({ pressed }) => [
                    { opacity: pressed ? 0.7 : 1 },
                  ]}
                >
                  <Text className="text-foreground font-semibold">Cancel</Text>
                </Pressable>
                <Pressable
                  onPress={handleSaveApiKey}
                  className="flex-1 bg-primary rounded-lg py-3 items-center"
                  style={({ pressed }) => [
                    { opacity: pressed ? 0.7 : 1 },
                  ]}
                >
                  <Text className="text-background font-semibold">Save</Text>
                </Pressable>
              </View>
            </View>
          )}

          {/* Update API Key Button */}
          <Pressable
            onPress={() => {
              setShowApiInput(!showApiInput);
              setTempApiKey(apiKey);
            }}
            style={({ pressed }) => [
              {
                opacity: pressed ? 0.7 : 1,
              },
            ]}
          >
            <View className="bg-primary rounded-lg py-3 items-center">
              <Text className="text-background font-semibold">
                {apiKey ? "Update API Key" : "Add API Key"}
              </Text>
            </View>
          </Pressable>

          <Text className="text-xs text-muted mt-3 leading-relaxed">
            Get your free Gemini API key from{" "}
            <Text className="font-semibold">Google AI Studio</Text>. Visit{" "}
            <Text className="font-semibold">aistudio.google.com</Text> to create one.
          </Text>
        </View>

        {/* Chat History Section */}
        <View className="px-6 py-6 border-b border-border">
          <Text className="text-lg font-semibold text-foreground mb-4">
            Chat History
          </Text>
          <View className="bg-surface rounded-lg px-4 py-3 mb-3">
            <Text className="text-sm text-muted mb-1">Total Messages</Text>
            <Text className="text-2xl font-bold text-foreground">
              {messageCount}
            </Text>
          </View>
          <Pressable
            onPress={handleClearHistory}
            style={({ pressed }) => [
              {
                opacity: pressed ? 0.7 : 1,
              },
            ]}
          >
            <View className="bg-error rounded-lg py-3 items-center">
              <Text className="text-background font-semibold">
                Clear Chat History
              </Text>
            </View>
          </Pressable>
        </View>

        {/* About Section */}
        <View className="px-6 py-6">
          <Text className="text-lg font-semibold text-foreground mb-4">
            About
          </Text>
          <View className="bg-surface rounded-lg px-4 py-4 gap-3">
            <View>
              <Text className="text-sm text-muted">App Name</Text>
              <Text className="text-base text-foreground font-semibold">
                LORNA
              </Text>
            </View>
            <View>
              <Text className="text-sm text-muted">Version</Text>
              <Text className="text-base text-foreground font-semibold">
                1.0.0
              </Text>
            </View>
            <View>
              <Text className="text-sm text-muted">AI Model</Text>
              <Text className="text-base text-foreground font-semibold">
                Gemini Pro
              </Text>
            </View>
          </View>
        </View>
      </ScrollView>
    </ScreenContainer>
  );
}
