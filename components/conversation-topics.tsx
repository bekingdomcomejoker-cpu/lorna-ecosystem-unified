import React from "react";
import { View, Text, Pressable, ScrollView } from "react-native";
import * as Haptics from "expo-haptics";
import { useColors } from "@/hooks/use-colors";

interface ConversationTopic {
  id: string;
  title: string;
  description: string;
  emoji: string;
}

const TOPICS: ConversationTopic[] = [
  {
    id: "1",
    title: "Ask a Question",
    description: "Get answers to any question",
    emoji: "❓",
  },
  {
    id: "2",
    title: "Creative Writing",
    description: "Write stories and poetry",
    emoji: "✍️",
  },
  {
    id: "3",
    title: "Learn Something",
    description: "Explore new topics and ideas",
    emoji: "📚",
  },
  {
    id: "4",
    title: "Brainstorm Ideas",
    description: "Generate creative concepts",
    emoji: "💡",
  },
  {
    id: "5",
    title: "Code Help",
    description: "Get programming assistance",
    emoji: "💻",
  },
  {
    id: "6",
    title: "Analyze Text",
    description: "Break down and discuss content",
    emoji: "🔍",
  },
];

interface ConversationTopicsProps {
  onSelectTopic: (topic: ConversationTopic) => void;
}

export function ConversationTopics({ onSelectTopic }: ConversationTopicsProps) {
  const colors = useColors();

  const handleTopicPress = (topic: ConversationTopic) => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
    onSelectTopic(topic);
  };

  return (
    <ScrollView
      horizontal
      showsHorizontalScrollIndicator={false}
      contentContainerStyle={{ paddingHorizontal: 16, gap: 8 }}
    >
      {TOPICS.map((topic) => (
        <Pressable
          key={topic.id}
          onPress={() => handleTopicPress(topic)}
          style={({ pressed }) => [
            {
              opacity: pressed ? 0.7 : 1,
              transform: [{ scale: pressed ? 0.95 : 1 }],
            },
          ]}
        >
          <View
            className="bg-surface rounded-xl px-4 py-3 border border-border"
            style={{
              minWidth: 140,
            }}
          >
            <Text className="text-2xl mb-1">{topic.emoji}</Text>
            <Text className="text-sm font-semibold text-foreground">
              {topic.title}
            </Text>
            <Text className="text-xs text-muted mt-1">
              {topic.description}
            </Text>
          </View>
        </Pressable>
      ))}
    </ScrollView>
  );
}
