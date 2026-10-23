package com.example.model

enum class SocialPlatform(val displayName: String, val iconLabel: String, val tag: String) {
    INSTAGRAM("Instagram", "IG", "#InstaReady"),
    TIKTOK("TikTok", "TT", "#FYP"),
    FACEBOOK("Facebook", "FB", "#Social"),
    WHATSAPP("WhatsApp", "WA", "#Status")
}

enum class CaptionTone(val displayName: String, val emoji: String) {
    FUNNY("Funny", "😂"),
    COOL("Cool", "😎"),
    ROMANTIC("Romantic", "💖"),
    PROFESSIONAL("Professional", "💼"),
    MOTIVATIONAL("Motivational", "🔥"),
    SAD("Sad", "🌧️"),
    AESTHETIC("Aesthetic", "✨")
}

enum class CaptionLength(val displayName: String, val hint: String) {
    SHORT("Short", "1-2 lines"),
    MEDIUM("Medium", "3-4 lines"),
    LONG("Long", "Deep story")
}

enum class QuickCategory(val displayName: String, val emoji: String, val samplePrompt: String) {
    LOVE("Love", "❤️", "Romantic dinner under city lights"),
    ATTITUDE("Attitude", "🕶️", "Leveling up and focusing on myself"),
    TRAVEL("Travel", "✈️", "Golden hour sunset by the Mediterranean coast"),
    BUSINESS("Business", "📈", "Launching new creative strategy and milestones"),
    FRIENDSHIP("Friendship", "🤝", "Unplanned late night drives with best friends"),
    GYM("Gym", "💪", "Consistent daily discipline and heavy lifting"),
    FOOD("Food", "🍕", "Tasting homemade artisan woodfired sourdough"),
    DAILY_LIFE("Daily Life", "☕", "Quiet rainy morning with black coffee and notebooks")
}

data class CaptionItem(
    val id: String,
    val text: String,
    val platform: String,
    val tone: String,
    val category: String,
    val length: String,
    val language: String,
    val hasEmojis: Boolean,
    val hasHashtags: Boolean,
    val isFavorite: Boolean = false,
    val isAiGenerated: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
