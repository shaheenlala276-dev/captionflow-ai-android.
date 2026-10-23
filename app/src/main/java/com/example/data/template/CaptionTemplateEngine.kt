package com.example.data.template

import com.example.model.CaptionLength
import com.example.model.CaptionTone
import com.example.model.SocialPlatform
import java.util.UUID

object CaptionTemplateEngine {

    fun generateCaptions(
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
        val cleanTopic = topic.trim()
        val hashtags = if (includeHashtags) buildHashtags(platform, category, cleanTopic) else ""
        val results = mutableListOf<String>()

        for (i in 0 until count) {
            val body = generateBody(cleanTopic, platform, tone, category, length, i)
            val withEmojis = if (includeEmojis) injectEmojis(body, tone, category) else removeEmojis(body)
            val fullCaption = if (hashtags.isNotEmpty()) {
                "$withEmojis\n\n$hashtags"
            } else {
                withEmojis
            }
            results.add(fullCaption.trim())
        }

        return results
    }

    private fun generateBody(
        topic: String,
        platform: SocialPlatform,
        tone: CaptionTone,
        category: String,
        length: CaptionLength,
        variationIndex: Int
    ): String {
        return when (tone) {
            CaptionTone.FUNNY -> getFunnyVariants(topic, platform, length, variationIndex)
            CaptionTone.COOL -> getCoolVariants(topic, platform, length, variationIndex)
            CaptionTone.ROMANTIC -> getRomanticVariants(topic, platform, length, variationIndex)
            CaptionTone.PROFESSIONAL -> getProfessionalVariants(topic, platform, length, variationIndex)
            CaptionTone.MOTIVATIONAL -> getMotivationalVariants(topic, platform, length, variationIndex)
            CaptionTone.SAD -> getSadVariants(topic, platform, length, variationIndex)
            CaptionTone.AESTHETIC -> getAestheticVariants(topic, platform, length, variationIndex)
        }
    }

    private fun getFunnyVariants(topic: String, platform: SocialPlatform, length: CaptionLength, index: Int): String {
        val hooks = listOf(
            "I told myself I wouldn't do this, but $topic proved me completely wrong.",
            "My plans vs reality with $topic. Honestly? 10/10 wouldn't change a thing.",
            "Please act like $topic was 100% planned and totally intentional.",
            "Running on iced caffeine and $topic. Please send backup.",
            "Proof that $topic is the only cardio I actually enjoy."
        )
        val hook = hooks[index % hooks.size]

        return when (length) {
            CaptionLength.SHORT -> hook
            CaptionLength.MEDIUM -> "$hook\n\nNot saying I am obsessed, but I definitely didn't see this plot twist coming."
            CaptionLength.LONG -> "$hook\n\nIf anyone asks, this is what professional multi-tasking looks like. Drop your honest rating below, but keep it friendly because my feelings are fragile today!"
        }
    }

    private fun getCoolVariants(topic: String, platform: SocialPlatform, length: CaptionLength, index: Int): String {
        val hooks = listOf(
            "Low key, high standards: $topic.",
            "Vibes speak louder than captions. Caught in the moment with $topic.",
            "Unbothered, moisturized, in my lane, and all about $topic.",
            "Not trying to make it look effortless, it just is with $topic.",
            "Main character energy activated: $topic."
        )
        val hook = hooks[index % hooks.size]

        return when (length) {
            CaptionLength.SHORT -> hook
            CaptionLength.MEDIUM -> "$hook\n\nQuiet confidence always turns the right heads."
            CaptionLength.LONG -> "$hook\n\nWe don't chase, we attract. Making every frame count and letting the genuine energy do the talking. What's on your agenda today?"
        }
    }

    private fun getRomanticVariants(topic: String, platform: SocialPlatform, length: CaptionLength, index: Int): String {
        val hooks = listOf(
            "Every love song starts making complete sense around $topic.",
            "In a room full of art, my eyes would still wander to $topic.",
            "Some moments feel like they were written in the constellations: $topic.",
            "Home isn't a place, it's feeling this warmth with $topic.",
            "Finding poetry in the quietest seconds of $topic."
        )
        val hook = hooks[index % hooks.size]

        return when (length) {
            CaptionLength.SHORT -> hook
            CaptionLength.MEDIUM -> "$hook\n\nGrateful for the little chapters that make life feel so timeless."
            CaptionLength.LONG -> "$hook\n\nThere's something magical about slowing down enough to truly notice how beautiful things can be. Holding onto this memory a little tighter today."
        }
    }

    private fun getProfessionalVariants(topic: String, platform: SocialPlatform, length: CaptionLength, index: Int): String {
        val hooks = listOf(
            "Transforming strategic vision into tangible progress: reflections on $topic.",
            "Key takeaway from this week's milestone on $topic: execution eats strategy for breakfast.",
            "Building sustainably requires patience, intentional design, and deep focus on $topic.",
            "Excited to share our perspective on elevating standards within $topic.",
            "Consistency in the fundamentals always creates exponential returns: diving into $topic."
        )
        val hook = hooks[index % hooks.size]

        return when (length) {
            CaptionLength.SHORT -> hook
            CaptionLength.MEDIUM -> "$hook\n\nWhat methodologies have made the biggest difference in your team's workflow lately?"
            CaptionLength.LONG -> "$hook\n\nWhen we align clarity of purpose with rigorous execution, the compounding results speak for themselves. I'd love to hear how fellow leaders approach this challenge in the comments below."
        }
    }

    private fun getMotivationalVariants(topic: String, platform: SocialPlatform, length: CaptionLength, index: Int): String {
        val hooks = listOf(
            "Your future self will thank you for showing up for $topic today.",
            "Don't count the days; make $topic count.",
            "Small disciplines repeated daily lead to massive breakthroughs: $topic.",
            "The difference between where you are and where you want to be is $topic.",
            "No shortcuts, no excuses. Just pure dedication to $topic."
        )
        val hook = hooks[index % hooks.size]

        return when (length) {
            CaptionLength.SHORT -> hook
            CaptionLength.MEDIUM -> "$hook\n\nKeep pushing even when nobody is watching. That's when real champions are made."
            CaptionLength.LONG -> "$hook\n\nThe road isn't always easy, but remember why you started. Every step forward is a victory against doubt. Commit to your journey and trust the process."
        }
    }

    private fun getSadVariants(topic: String, platform: SocialPlatform, length: CaptionLength, index: Int): String {
        val hooks = listOf(
            "Some chapters hurt to read, even when they're about $topic.",
            "Quiet thoughts on a heavy day: holding space for $topic.",
            "Healing isn't linear, and that's okay: feeling through $topic.",
            "Missing the warmth of simpler days with $topic.",
            "It’s okay if all you did today was survive through $topic."
        )
        val hook = hooks[index % hooks.size]

        return when (length) {
            CaptionLength.SHORT -> hook
            CaptionLength.MEDIUM -> "$hook\n\nTaking it one breath at a time."
            CaptionLength.LONG -> "$hook\n\nWe don't always need to have all the answers. Sometimes acknowledging the silence and honoring our emotions is the bravest thing we can do."
        }
    }

    private fun getAestheticVariants(topic: String, platform: SocialPlatform, length: CaptionLength, index: Int): String {
        val hooks = listOf(
            "soft shadows & serene corners // $topic",
            "golden hour hues and whispers of $topic.",
            "finding calm amidst the noise: $topic.",
            "filtered light, vintage memories, and $topic.",
            "poetry in motion, living inside $topic."
        )
        val hook = hooks[index % hooks.size]

        return when (length) {
            CaptionLength.SHORT -> hook
            CaptionLength.MEDIUM -> "$hook\n\ncurating moments that feel like home."
            CaptionLength.LONG -> "$hook\n\nthere is a gentle beauty in slowing down and taking note of the textures around us. breathing it all in."
        }
    }

    private fun injectEmojis(text: String, tone: CaptionTone, category: String): String {
        val toneEmojis = when (tone) {
            CaptionTone.FUNNY -> " 😂💀✨"
            CaptionTone.COOL -> " 🕶️⚡🔥"
            CaptionTone.ROMANTIC -> " 💖🌹✨"
            CaptionTone.PROFESSIONAL -> " 📊💼🎯"
            CaptionTone.MOTIVATIONAL -> " 🚀💪🔥"
            CaptionTone.SAD -> " 🌧️🤍🕊️"
            CaptionTone.AESTHETIC -> " ✨🌙☕"
        }
        return "$text$toneEmojis"
    }

    private fun removeEmojis(text: String): String {
        // Strip out common emoji unicodes if requested
        return text.replace(Regex("[\\p{So}\\p{Cn}]"), "").trim()
    }

    private fun buildHashtags(platform: SocialPlatform, category: String, topic: String): String {
        val baseTags = when (platform) {
            SocialPlatform.INSTAGRAM -> listOf("#instagood", "#photooftheday", "#reels", "#explorepage")
            SocialPlatform.TIKTOK -> listOf("#fyp", "#foryou", "#viral", "#trending")
            SocialPlatform.FACEBOOK -> listOf("#community", "#story", "#highlights")
            SocialPlatform.WHATSAPP -> listOf("#status", "#vibes", "#mood")
        }

        val categoryTags = when (category.lowercase()) {
            "love" -> listOf("#love", "#couplegoals", "#forever", "#romance")
            "attitude" -> listOf("#attitude", "#bossmode", "#selflove", "#swag")
            "travel" -> listOf("#travelgram", "#wanderlust", "#adventure", "#vacation")
            "business" -> listOf("#entrepreneur", "#leadership", "#growth", "#mindset")
            "friendship" -> listOf("#besties", "#friendsforever", "#squadgoals", "#memories")
            "gym" -> listOf("#fitness", "#workout", "#gymmotivation", "#fitlife")
            "food" -> listOf("#foodie", "#delicious", "#yummy", "#instafood")
            "daily life" -> listOf("#dailylife", "#lifestyle", "#simplethings", "#blessed")
            else -> listOf("#captionflow", "#trending", "#mood")
        }

        val cleanTopicTag = "#" + topic.filter { it.isLetterOrDigit() }.take(14).lowercase()
        val allTags = (categoryTags + baseTags + if (cleanTopicTag.length > 2) listOf(cleanTopicTag) else emptyList()).distinct()
        return allTags.take(6).joinToString(" ")
    }
}
