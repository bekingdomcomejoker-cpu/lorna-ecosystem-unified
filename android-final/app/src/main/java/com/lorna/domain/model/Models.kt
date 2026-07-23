package com.lorna.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
)

@Entity(tableName = "models")
data class ModelEntity(
    @PrimaryKey val id: String,
    val name: String,
    val path: String,
    val sizeBytes: Long,
    val genTps: Double = 0.0,
    val isActive: Boolean = false
)

data class LensAnalysis(
    val phonetic: Double,
    val semantic: Double,
    val morphological: Double,
    val symbolic: Double,
    val composite: Double,
    val resonance: Double = 0.0
)

data class MemoryNode(
    val id: String,
    val summary: String,
    val lensVector: String,
    val timestamp: Long
)
