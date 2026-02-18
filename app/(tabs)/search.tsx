import React, { useState, useEffect } from "react";
import {
  ScrollView,
  Text,
  View,
  TextInput,
  FlatList,
  Pressable,
  ActivityIndicator,
} from "react-native";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { ScreenContainer } from "@/components/screen-container";
import { useColors } from "@/hooks/use-colors";

interface Message {
  id: string;
  text: string;
  sender: "user" | "ai";
  timestamp: Date;
}

const STORAGE_KEY = "lorna_messages";

export default function SearchScreen() {
  const colors = useColors();
  const [searchQuery, setSearchQuery] = useState("");
  const [allMessages, setAllMessages] = useState<Message[]>([]);
  const [filteredMessages, setFilteredMessages] = useState<Message[]>([]);
  const [isSearching, setIsSearching] = useState(false);

  useEffect(() => {
    loadMessages();
  }, []);

  useEffect(() => {
    if (searchQuery.trim()) {
      performSearch();
    } else {
      setFilteredMessages([]);
    }
  }, [searchQuery]);

  const loadMessages = async () => {
    try {
      const stored = await AsyncStorage.getItem(STORAGE_KEY);
      if (stored) {
        const parsed = JSON.parse(stored);
        const messages = parsed.map((msg: any) => ({
          ...msg,
          timestamp: new Date(msg.timestamp),
        }));
        setAllMessages(messages);
      }
    } catch (error) {
      console.error("Failed to load messages:", error);
    }
  };

  const performSearch = async () => {
    setIsSearching(true);
    const query = searchQuery.toLowerCase();
    const results = allMessages.filter((msg) =>
      msg.text.toLowerCase().includes(query)
    );
    setFilteredMessages(results);
    setIsSearching(false);
  };

  const renderSearchResult = ({ item }: { item: Message }) => {
    const highlightedText = item.text
      .split(new RegExp(`(${searchQuery})`, "gi"))
      .map((part, idx) =>
        part.toLowerCase() === searchQuery.toLowerCase()
          ? `[${part}]`
          : part
      )
      .join("");

    return (
      <Pressable
        onPress={() => {
          // In a real app, this would navigate to the chat screen with the message highlighted
        }}
        style={({ pressed }) => [{ opacity: pressed ? 0.7 : 1 }]}
      >
        <View className="bg-surface rounded-lg p-4 mb-3 border border-border">
          <View className="flex-row items-center mb-2 gap-2">
            <View
              className={`w-2 h-2 rounded-full ${
                item.sender === "user" ? "bg-primary" : "bg-success"
              }`}
            />
            <Text className="text-xs text-muted font-semibold">
              {item.sender === "user" ? "You" : "LORNA"}
            </Text>
            <Text className="text-xs text-muted ml-auto">
              {item.timestamp.toLocaleDateString([], {
                month: "short",
                day: "numeric",
                hour: "2-digit",
                minute: "2-digit",
              })}
            </Text>
          </View>
          <Text
            numberOfLines={3}
            className="text-sm text-foreground leading-relaxed"
          >
            {highlightedText}
          </Text>
        </View>
      </Pressable>
    );
  };

  return (
    <ScreenContainer className="flex-1 bg-background">
      <View className="flex-1">
        {/* Header */}
        <View className="px-6 py-4 border-b border-border">
          <Text className="text-2xl font-bold text-foreground mb-4">
            Search Messages
          </Text>

          {/* Search Input */}
          <View className="flex-row items-center bg-surface rounded-lg px-4 py-3 border border-border">
            <TextInput
              value={searchQuery}
              onChangeText={setSearchQuery}
              placeholder="Search conversations..."
              placeholderTextColor={colors.muted}
              className="flex-1 text-foreground"
            />
            {isSearching && (
              <ActivityIndicator
                size="small"
                color={colors.primary}
              />
            )}
          </View>
        </View>

        {/* Results */}
        {searchQuery.trim() ? (
          <View className="flex-1">
            {filteredMessages.length > 0 ? (
              <FlatList
                data={filteredMessages}
                renderItem={renderSearchResult}
                keyExtractor={(item) => item.id}
                contentContainerStyle={{ padding: 16 }}
                scrollEnabled={true}
              />
            ) : (
              <View className="flex-1 items-center justify-center px-6">
                <Text className="text-lg text-muted text-center">
                  No messages found matching "{searchQuery}"
                </Text>
              </View>
            )}
          </View>
        ) : (
          <View className="flex-1 items-center justify-center px-6">
            <Text className="text-lg text-muted text-center">
              Type something to search your chat history
            </Text>
            <Text className="text-sm text-muted text-center mt-2">
              Total messages: {allMessages.length}
            </Text>
          </View>
        )}
      </View>
    </ScreenContainer>
  );
}
