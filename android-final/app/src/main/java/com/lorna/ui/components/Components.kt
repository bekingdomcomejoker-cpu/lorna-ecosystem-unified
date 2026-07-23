package com.lorna.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lorna.domain.model.MessageEntity
import com.lorna.ui.viewmodel.ChatUiState

@Composable
fun ChatMessage(message: MessageEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (message.isUser) Color(0xFF00BCD4) else Color(0xFF2A2A2A),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) Color.Black else Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun MetricsPanel(uiState: ChatUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .padding(12.dp)
    ) {
        // Metrics Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricCard("Gen t/s", "%.1f".format(uiState.genTps), Color(0xFF4CAF50))
            MetricCard("Thermal", "%.1f°C".format(uiState.thermal), getThermalColor(uiState.thermal))
            MetricCard("Memory", "${uiState.memoryPercent}%", getMemoryColor(uiState.memoryPercent))
            MetricCard("Model", "DeepSeek", Color(0xFF00BCD4))
        }

        // Axiom Reflection
        if (uiState.axiomReflection.isNotEmpty()) {
            Text(
                text = uiState.axiomReflection,
                color = Color(0xFF00BCD4),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Memory Compression
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompressionPass("Pass 1", uiState.isProcessing)
            CompressionPass("Pass 2", false)
        }
    }
}

@Composable
fun MetricCard(label: String, value: String, color: Color) {
    Column(
        modifier = Modifier
            .background(Color(0xFF2A2A2A), RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 10.sp, color = Color(0xFFB0B0B0))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun CompressionPass(label: String, isActive: Boolean) {
    Box(
        modifier = Modifier
            .background(
                color = if (isActive) Color(0xFF00BCD4).copy(alpha = 0.3f) else Color(0xFF2A2A2A),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isActive) Color(0xFF00BCD4) else Color(0xFF666666)
        )
    }
}

fun getThermalColor(temp: Double): Color {
    return when {
        temp < 40 -> Color(0xFF4CAF50)
        temp < 50 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }
}

fun getMemoryColor(memory: Int): Color {
    return when {
        memory < 70 -> Color(0xFF4CAF50)
        memory < 80 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }
}
