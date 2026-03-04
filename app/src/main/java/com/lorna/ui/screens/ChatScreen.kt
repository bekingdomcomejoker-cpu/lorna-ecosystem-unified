package com.lorna.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorna.ui.components.ChatMessage
import com.lorna.ui.components.MetricsPanel
import com.lorna.ui.viewmodel.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel = viewModel()) {
    val messages by viewModel.messages.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        // Header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            color = Color(0xFF1E1E1E),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "🧠 LORNA v3",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00BCD4)
                )
                Text(
                    text = "● LOCAL",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50)
                )
            }
        }

        // Metrics Panel
        MetricsPanel(uiState = uiState)

        // Messages Area
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                ChatMessage(message = message)
            }
        }

        // Input Area
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            color = Color(0xFF1E1E1E),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    placeholder = { Text("Type message...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF2A2A2A),
                        unfocusedContainerColor = Color(0xFF2A2A2A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    ),
                    singleLine = true,
                    enabled = !uiState.isProcessing
                )

                IconButton(
                    onClick = { /* Voice input */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice",
                        tint = Color(0xFF00BCD4)
                    )
                }

                IconButton(
                    onClick = {
                        if (inputText.isNotEmpty()) {
                            viewModel.sendMessage(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier.size(40.dp),
                    enabled = inputText.isNotEmpty() && !uiState.isProcessing
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (uiState.isProcessing) Color.Gray else Color(0xFF00BCD4)
                    )
                }
            }
        }

        // Footer Status
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            color = Color(0xFF0A0A0A)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("✓ Offline", fontSize = 10.sp, color = Color(0xFF4CAF50))
                Text("✓ Streaming", fontSize = 10.sp, color = Color(0xFF4CAF50))
                Text("✓ Memory", fontSize = 10.sp, color = Color(0xFF4CAF50))
                Text("✓ Sovereign", fontSize = 10.sp, color = Color(0xFF4CAF50))
            }
        }
    }
}
