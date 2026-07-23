import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  Pressable,
  Modal,
  ActivityIndicator,
  Alert,
} from "react-native";
// Speech-to-text would require integration with an external API service
import * as Haptics from "expo-haptics";
import { useColors } from "@/hooks/use-colors";

interface VoiceInputProps {
  isVisible: boolean;
  onClose: () => void;
  onTranscriptionComplete: (text: string) => void;
}

export function VoiceInput({
  isVisible,
  onClose,
  onTranscriptionComplete,
}: VoiceInputProps) {
  const colors = useColors();
  const [isRecording, setIsRecording] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);
  const [transcript, setTranscript] = useState("");

  useEffect(() => {
    if (isVisible && !isRecording) {
      startRecording();
    }
  }, [isVisible]);

  const startRecording = async () => {
    try {
      setIsRecording(true);
      setTranscript("");
      Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);

      // Note: Expo Speech API doesn't have built-in speech-to-text in SDK 54
      // This is a placeholder implementation. In production, you would integrate
      // with a service like Google Cloud Speech-to-Text or OpenAI Whisper API
      
      // For now, we'll show a message to the user
      Alert.alert(
        "Voice Input",
        "Speech-to-text requires integration with an external API. Please type your message instead, or provide your API credentials in settings."
      );
      onClose();
    } catch (error) {
      console.error("Error starting recording:", error);
      Alert.alert("Error", "Failed to start recording");
      onClose();
    }
  };

  const stopRecording = async () => {
    try {
      setIsRecording(false);
      setIsProcessing(true);
      Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);

      // Simulate processing
      setTimeout(() => {
        if (transcript.trim()) {
          onTranscriptionComplete(transcript);
        }
        setIsProcessing(false);
        onClose();
      }, 500);
    } catch (error) {
      console.error("Error stopping recording:", error);
      Alert.alert("Error", "Failed to process recording");
      setIsProcessing(false);
      onClose();
    }
  };

  const handleCancel = () => {
    setIsRecording(false);
    setTranscript("");
    onClose();
  };

  return (
    <Modal
      visible={isVisible}
      transparent
      animationType="fade"
      onRequestClose={handleCancel}
    >
      <View
        className="flex-1 bg-black/50 items-center justify-center"
        style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
      >
        <View
          className="bg-background rounded-3xl p-8 w-4/5 items-center"
          style={{
            backgroundColor: colors.background,
          }}
        >
          {/* Title */}
          <Text className="text-2xl font-bold text-foreground mb-6">
            Voice Input
          </Text>

          {/* Recording Indicator */}
          {isRecording && (
            <View className="mb-6 items-center">
              <View className="w-20 h-20 rounded-full bg-primary/20 items-center justify-center mb-4">
                <View
                  className="w-16 h-16 rounded-full bg-primary items-center justify-center"
                  style={{
                    opacity: 0.8,
                  }}
                >
                <View
                  className="w-4 h-4 rounded-full bg-background"
                />
                </View>
              </View>
              <Text className="text-base text-muted">Listening...</Text>
            </View>
          )}

          {/* Transcript Display */}
          {transcript && (
            <View className="w-full bg-surface rounded-xl p-4 mb-6">
              <Text className="text-sm text-muted mb-2">Transcript:</Text>
              <Text className="text-base text-foreground">{transcript}</Text>
            </View>
          )}

          {/* Processing Indicator */}
          {isProcessing && (
            <View className="mb-6 items-center">
              <ActivityIndicator
                size="large"
                color={colors.primary}
              />
              <Text className="text-base text-muted mt-3">Processing...</Text>
            </View>
          )}

          {/* Buttons */}
          <View className="flex-row gap-3 w-full">
            <Pressable
              onPress={handleCancel}
              className="flex-1 bg-surface rounded-lg py-3 items-center"
              style={({ pressed }) => [
                { opacity: pressed ? 0.7 : 1 },
              ]}
            >
              <Text className="text-foreground font-semibold">Cancel</Text>
            </Pressable>

            {isRecording && (
              <Pressable
                onPress={stopRecording}
                className="flex-1 bg-primary rounded-lg py-3 items-center"
                style={({ pressed }) => [
                  { opacity: pressed ? 0.7 : 1 },
                ]}
              >
                <Text className="text-background font-semibold">Send</Text>
              </Pressable>
            )}
          </View>

          {/* Info Text */}
          <Text className="text-xs text-muted text-center mt-4 leading-relaxed">
            Note: Voice input requires integration with a speech-to-text service. For now, please use text input.
          </Text>
        </View>
      </View>
    </Modal>
  );
}
