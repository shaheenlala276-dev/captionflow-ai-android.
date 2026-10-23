package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.example.data.ai.GeminiClient
import com.example.data.ai.GeminiContent
import com.example.data.ai.GeminiGenerationConfig
import com.example.data.ai.GeminiPart
import com.example.data.ai.GeminiRequest
import com.example.data.local.AppDatabase
import com.example.data.local.CaptionEntity
import com.example.data.template.CaptionTemplateEngine
import com.example.model.CaptionItem
import com.example.model.CaptionLength
import com.example.model.CaptionTone
import com.example.model.SocialPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

sealed class GenerationResult {
    data class Success(val captions: List<CaptionItem>, val isAiGenerated: Boolean, val message: String) : GenerationResult()
    data class Error(val message: String) : GenerationResult()
}

class CaptionRepository(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val dao = database.captionDao()

    val allHistory: Flow<List<CaptionItem>> = dao.getAllHistory().map { list ->
        list.map { it.toCaptionItem() }
    }

    val favorites: Flow<List<CaptionItem>> = dao.getFavorites().map { list ->
        list.map { it.toCaptionItem() }
    }

    fun searchCaptions(query: String): Flow<List<CaptionItem>> = dao.searchCaptions(query).map { list ->
        list.map { it.toCaptionItem() }
    }

    suspend fun generateCaptions(
        topic: String,
        platform: SocialPlatform,
        tone: CaptionTone,
        category: String,
        length: CaptionLength,
        language: String,
        includeEmojis: Boolean,
        includeHashtags: Boolean,
        count: Int
    ): GenerationResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val hasValidApiKey = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

        if (hasValidApiKey) {
            try {
                val aiCaptions = callGeminiApi(
                    apiKey = apiKey,
                    topic = topic,
                    platform = platform,
                    tone = tone,
                    category = category,
                    length = length,
                    language = language,
                    includeEmojis = includeEmojis,
                    includeHashtags = includeHashtags,
                    count = count
                )

                if (aiCaptions.isNotEmpty()) {
                    val captionItems = aiCaptions.map { text ->
                        CaptionItem(
                            id = UUID.randomUUID().toString(),
                            text = text,
                            platform = platform.displayName,
                            tone = tone.displayName,
                            category = category,
                            length = length.displayName,
                            language = language,
                            hasEmojis = includeEmojis,
                            hasHashtags = includeHashtags,
                            isFavorite = false,
                            isAiGenerated = true,
                            timestamp = System.currentTimeMillis()
                        )
                    }
                    dao.insertAll(captionItems.map { CaptionEntity.fromCaptionItem(it) })
                    return@withContext GenerationResult.Success(
                        captions = captionItems,
                        isAiGenerated = true,
                        message = "Generated $count AI captions using Gemini 3.5 Flash!"
                    )
                }
            } catch (e: Exception) {
                Log.w("CaptionRepository", "Gemini API call failed, falling back to smart templates: ${e.message}")
            }
        }

        // Fallback to intelligent built-in template mode
        val templateCaptions = CaptionTemplateEngine.generateCaptions(
            topic = topic,
            platform = platform,
            tone = tone,
            category = category,
            length = length,
            language = language,
            includeEmojis = includeEmojis,
            includeHashtags = includeHashtags,
            count = count
        )

        val captionItems = templateCaptions.map { text ->
            CaptionItem(
                id = UUID.randomUUID().toString(),
                text = text,
                platform = platform.displayName,
                tone = tone.displayName,
                category = category,
                length = length.displayName,
                language = language,
                hasEmojis = includeEmojis,
                hasHashtags = includeHashtags,
                isFavorite = false,
                isAiGenerated = false,
                timestamp = System.currentTimeMillis()
            )
        }

        dao.insertAll(captionItems.map { CaptionEntity.fromCaptionItem(it) })

        val note = if (!hasValidApiKey) {
            "Generated using Built-in Smart Templates (Add Gemini API key in Secrets for custom AI)."
        } else {
            "Generated using Built-in Smart Templates (Network/API fallback mode)."
        }

        GenerationResult.Success(
            captions = captionItems,
            isAiGenerated = false,
            message = note
        )
    }

    private suspend fun callGeminiApi(
        apiKey: String,
        topic: String,
        platform: SocialPlatform,
        tone: CaptionTone,
        category: String,
        length: CaptionLength,
        language: String,
        includeEmojis: Boolean,
        includeHashtags: Boolean,
        count: Int
    ): List<String> {
        val lengthDesc = when (length) {
            CaptionLength.SHORT -> "1-2 short punchy sentences"
            CaptionLength.MEDIUM -> "3-4 sentences with strong narrative"
            CaptionLength.LONG -> "a deeper storytelling caption (5-8 sentences)"
        }

        val emojiInstruction = if (includeEmojis) "Include tasteful, expressive emojis." else "Do NOT include emojis."
        val hashtagInstruction = if (includeHashtags) "Include 4-6 highly relevant hashtags at the end." else "Do NOT include hashtags."

        val prompt = """
            You are CaptionFlow AI, an elite social media copywriter.
            Create exactly $count distinct, ready-to-post caption variations for:
            - Platform: ${platform.displayName}
            - Topic/Description: "$topic"
            - Tone: ${tone.displayName}
            - Category: $category
            - Length: $lengthDesc
            - Language: $language
            - Emojis: $emojiInstruction
            - Hashtags: $hashtagInstruction

            Formatting Rules:
            Separate each distinct caption variation with the exact delimiter:
            ---CAPTION---
            Do NOT include numbering like "Option 1:" or "Caption 1:". Output ONLY the caption variations separated by ---CAPTION---.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(GeminiPart(text = prompt))
                )
            ),
            generationConfig = GeminiGenerationConfig(temperature = 0.85f)
        )

        val response = GeminiClient.service.generateContent(apiKey, request)
        val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""

        val rawList = text.split("---CAPTION---")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        return if (rawList.isNotEmpty()) {
            rawList.take(count)
        } else {
            listOf(text.trim()).filter { it.isNotEmpty() }
        }
    }

    suspend fun toggleFavorite(caption: CaptionItem) = withContext(Dispatchers.IO) {
        val newStatus = !caption.isFavorite
        dao.updateFavorite(caption.id, newStatus)
    }

    suspend fun updateCaptionText(id: String, newText: String) = withContext(Dispatchers.IO) {
        dao.updateText(id, newText)
    }

    suspend fun deleteCaption(id: String) = withContext(Dispatchers.IO) {
        dao.deleteById(id)
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        dao.clearAll()
    }
}
