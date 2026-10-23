package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.CaptionLength
import com.example.model.CaptionTone
import com.example.model.QuickCategory
import com.example.model.SocialPlatform
import com.example.ui.MainViewModel
import com.example.ui.theme.BrandAqua
import com.example.ui.theme.BrandAquaContainer
import com.example.ui.theme.BrandBorder
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandDarkCardElevated
import com.example.ui.theme.BrandDarkCharcoal
import com.example.ui.theme.BrandDarkSurface
import com.example.ui.theme.BrandLightGrey
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPinkContainer
import com.example.ui.theme.BrandSubtleText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val topic by viewModel.topic.collectAsState()
    val topicError by viewModel.topicError.collectAsState()
    val selectedPlatform by viewModel.selectedPlatform.collectAsState()
    val selectedTone by viewModel.selectedTone.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedLength by viewModel.selectedLength.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val includeEmojis by viewModel.includeEmojis.collectAsState()
    val includeHashtags by viewModel.includeHashtags.collectAsState()
    val variationCount by viewModel.variationCount.collectAsState()

    val scrollState = rememberScrollState()

    val languages = listOf("English", "Spanish", "French", "German", "Hindi", "Portuguese", "Italian", "Japanese")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandDarkCharcoal)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("screen_home")
    ) {
        // App Header & Branding
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(BrandAquaContainer)
                    .border(1.5.dp, BrandAqua, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "CaptionFlow AI Logo",
                    tint = BrandAqua,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "CaptionFlow AI",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BrandAqua
                )
                Text(
                    text = "Social Media Caption Maker",
                    fontSize = 13.sp,
                    color = BrandSubtleText
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Welcome banner
        Surface(
            color = BrandDarkCard,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BrandBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Generate High-Engagement Captions ✨",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = BrandLightGrey
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tailored for Instagram, TikTok, Facebook & WhatsApp. Choose your vibe, length, and let AI work its magic.",
                    fontSize = 13.sp,
                    color = BrandSubtleText,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 1. Platform Selector
        Text(
            text = "Select Platform",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandAqua
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SocialPlatform.values().forEach { platform ->
                val isSelected = platform == selectedPlatform
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.selectPlatform(platform) }
                        .testTag("platform_${platform.name.lowercase()}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) BrandAquaContainer else BrandDarkCard
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) BrandAqua else BrandBorder
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = platform.iconLabel,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) BrandAqua else BrandLightGrey
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = platform.displayName,
                            fontSize = 11.sp,
                            color = if (isSelected) BrandAqua else BrandSubtleText,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Category Chips
        Text(
            text = "Quick Categories",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandLightGrey
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickCategory.values().forEach { category ->
                val isSelected = selectedCategory == category.displayName
                Surface(
                    modifier = Modifier
                        .clickable { viewModel.applyQuickCategory(category) }
                        .testTag("category_${category.name.lowercase()}"),
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) BrandPinkContainer else BrandDarkCard,
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) BrandPink else BrandBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.emoji,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = category.displayName,
                            fontSize = 12.sp,
                            color = if (isSelected) BrandPink else BrandLightGrey,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Topic Input
        Text(
            text = "What is your post about? *",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandAqua
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = topic,
            onValueChange = { viewModel.onTopicChanged(it) },
            placeholder = {
                Text(
                    text = "e.g. Sunset beach run with friends, homemade latte art, new product launch...",
                    fontSize = 14.sp,
                    color = BrandSubtleText
                )
            },
            isError = topicError != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .testTag("input_topic"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = BrandLightGrey,
                unfocusedTextColor = BrandLightGrey,
                focusedBorderColor = BrandAqua,
                unfocusedBorderColor = BrandBorder,
                focusedContainerColor = BrandDarkCard,
                unfocusedContainerColor = BrandDarkCard,
                errorBorderColor = BrandPink,
                cursorColor = BrandAqua
            )
        )

        AnimatedVisibility(visible = topicError != null) {
            Text(
                text = topicError ?: "",
                color = BrandPink,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Tone Selector
        Text(
            text = "Caption Tone",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandLightGrey
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CaptionTone.values().forEach { tone ->
                val isSelected = tone == selectedTone
                Surface(
                    modifier = Modifier
                        .clickable { viewModel.selectTone(tone) }
                        .testTag("tone_${tone.name.lowercase()}"),
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) BrandAquaContainer else BrandDarkCard,
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) BrandAqua else BrandBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = tone.emoji, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tone.displayName,
                            fontSize = 13.sp,
                            color = if (isSelected) BrandAqua else BrandLightGrey,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. Caption Length
        Text(
            text = "Caption Length",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandLightGrey
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CaptionLength.values().forEach { length ->
                val isSelected = length == selectedLength
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.selectLength(length) }
                        .testTag("length_${length.name.lowercase()}"),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) BrandAquaContainer else BrandDarkCard,
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) BrandAqua else BrandBorder
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = length.displayName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) BrandAqua else BrandLightGrey
                        )
                        Text(
                            text = length.hint,
                            fontSize = 10.sp,
                            color = BrandSubtleText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 5. Language Selector
        Text(
            text = "Caption Language",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandLightGrey
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            languages.forEach { lang ->
                val isSelected = lang == selectedLanguage
                Surface(
                    modifier = Modifier
                        .clickable { viewModel.selectLanguage(lang) }
                        .testTag("lang_${lang.lowercase()}"),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) BrandAquaContainer else BrandDarkCard,
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) BrandAqua else BrandBorder
                    )
                ) {
                    Text(
                        text = lang,
                        fontSize = 12.sp,
                        color = if (isSelected) BrandAqua else BrandLightGrey,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 6. Toggles & Variations count
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BrandDarkCard),
            border = BorderStroke(1.dp, BrandBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Emojis toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Include Emojis",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrandLightGrey
                        )
                        Text(
                            text = "Adds expressive mood emojis",
                            fontSize = 11.sp,
                            color = BrandSubtleText
                        )
                    }
                    Switch(
                        checked = includeEmojis,
                        onCheckedChange = { viewModel.toggleEmojis(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = BrandDarkCharcoal,
                            checkedTrackColor = BrandAqua,
                            uncheckedThumbColor = BrandLightGrey,
                            uncheckedTrackColor = BrandDarkSurface
                        ),
                        modifier = Modifier.testTag("switch_emojis")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Hashtags toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Include Hashtags",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrandLightGrey
                        )
                        Text(
                            text = "Adds platform & topic trending tags",
                            fontSize = 11.sp,
                            color = BrandSubtleText
                        )
                    }
                    Switch(
                        checked = includeHashtags,
                        onCheckedChange = { viewModel.toggleHashtags(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = BrandDarkCharcoal,
                            checkedTrackColor = BrandAqua,
                            uncheckedThumbColor = BrandLightGrey,
                            uncheckedTrackColor = BrandDarkSurface
                        ),
                        modifier = Modifier.testTag("switch_hashtags")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Variation count selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Variations Count",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrandLightGrey
                        )
                        Text(
                            text = "Generate multiple options to choose from",
                            fontSize = 11.sp,
                            color = BrandSubtleText
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.setVariationCount(variationCount - 1) },
                            enabled = variationCount > 1,
                            modifier = Modifier
                                .size(34.dp)
                                .background(BrandDarkSurface, CircleShape)
                                .border(1.dp, BrandBorder, CircleShape)
                                .testTag("button_decrease_count")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease count",
                                tint = if (variationCount > 1) BrandLightGrey else BrandSubtleText,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = "$variationCount",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandAqua,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )

                        IconButton(
                            onClick = { viewModel.setVariationCount(variationCount + 1) },
                            enabled = variationCount < 5,
                            modifier = Modifier
                                .size(34.dp)
                                .background(BrandDarkSurface, CircleShape)
                                .border(1.dp, BrandBorder, CircleShape)
                                .testTag("button_increase_count")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase count",
                                tint = if (variationCount < 5) BrandLightGrey else BrandSubtleText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Prominent Aqua Generate Button
        Button(
            onClick = { viewModel.generateCaptions() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("button_generate"),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandAqua,
                contentColor = BrandDarkCharcoal
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Generate Captions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
