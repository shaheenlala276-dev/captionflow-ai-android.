package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.CaptionItem

@Entity(tableName = "captions")
data class CaptionEntity(
    @PrimaryKey val id: String,
    val text: String,
    val platform: String,
    val tone: String,
    val category: String,
    val length: String,
    val language: String,
    val hasEmojis: Boolean,
    val hasHashtags: Boolean,
    val isFavorite: Boolean,
    val isAiGenerated: Boolean,
    val timestamp: Long
) {
    fun toCaptionItem(): CaptionItem {
        return CaptionItem(
            id = id,
            text = text,
            platform = platform,
            tone = tone,
            category = category,
            length = length,
            language = language,
            hasEmojis = hasEmojis,
            hasHashtags = hasHashtags,
            isFavorite = isFavorite,
            isAiGenerated = isAiGenerated,
            timestamp = timestamp
        )
    }

    companion object {
        fun fromCaptionItem(item: CaptionItem): CaptionEntity {
            return CaptionEntity(
                id = item.id,
                text = item.text,
                platform = item.platform,
                tone = item.tone,
                category = item.category,
                length = item.length,
                language = item.language,
                hasEmojis = item.hasEmojis,
                hasHashtags = item.hasHashtags,
                isFavorite = item.isFavorite,
                isAiGenerated = item.isAiGenerated,
                timestamp = item.timestamp
            )
        }
    }
}
