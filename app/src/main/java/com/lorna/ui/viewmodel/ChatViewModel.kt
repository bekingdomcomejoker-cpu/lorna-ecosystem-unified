package com.lorna.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lorna.domain.core.LornaLinguisticCore
import com.lorna.domain.core.AxiomMirror
import com.lorna.domain.data.LornaDatabase
import com.lorna.domain.model.MessageEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class ChatUiState(
    val lastResonance: Double = 0.0,
    val isProcessing: Boolean = false,
    val memoryUsage: String = "1.2GB / 4GB",
    val genTps: Double = 15.2,
    val thermal: Double = 42.5,
    val memoryPercent: Int = 65,
    val axiomReflection: String = ""
)

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = LornaDatabase.getDatabase(application).lornaDao()
    private val core = LornaLinguisticCore()
    private val mirror = AxiomMirror()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    val messages: StateFlow<List<MessageEntity>> = dao.getAllMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }

            // 1. Save User Message
            val userMsg = MessageEntity(
                id = UUID.randomUUID().toString(),
                text = text,
                isUser = true,
                timestamp = System.currentTimeMillis()
            )
            dao.insertMessage(userMsg)

            // 2. Analyze
            val analysis = core.analyze(text)
            val reflection = mirror.reflect(analysis)

            // 3. Save Response
            val response = "Resonance detected at ${"%.2f".format(analysis.resonance)}. Witness confirmed."
            val responseMsg = MessageEntity(
                id = UUID.randomUUID().toString(),
                text = response,
                isUser = false,
                timestamp = System.currentTimeMillis()
            )
            dao.insertMessage(responseMsg)

            // 4. Update UI State
            _uiState.update {
                it.copy(
                    lastResonance = analysis.resonance,
                    isProcessing = false,
                    genTps = (8..30).random().toDouble() + kotlin.random.Random.nextDouble(),
                    thermal = (35..55).random().toDouble() + kotlin.random.Random.nextDouble(),
                    memoryPercent = (50..85).random(),
                    axiomReflection = reflection
                )
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            dao.clearMessages()
        }
    }
}
