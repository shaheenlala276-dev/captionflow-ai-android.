package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.CaptionCard
import com.example.ui.components.EditCaptionDialog
import com.example.ui.theme.BrandAqua
import com.example.ui.theme.BrandAquaContainer
import com.example.ui.theme.BrandBorder
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandDarkCharcoal
import com.example.ui.theme.BrandDarkSurface
import com.example.ui.theme.BrandLightGrey
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandSubtleText

@Composable
fun CreateScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isGenerating by viewModel.isGenerating.collectAsState()
    val captions by viewModel.currentBatch.collectAsState()
    val generationMessage by viewModel.generationMessage.collectAsState()
    val isLastAi by viewModel.isLastAi.collectAsState()
    val editingCaption by viewModel.editingCaption.collectAsState()

    // Dialog for editing
    editingCaption?.let { item ->
        EditCaptionDialog(
            caption = item,
            onDismiss = { viewModel.closeEditDialog() },
            onSave = { newText -> viewModel.saveEditedCaption(newText) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandDarkCharcoal)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("screen_create")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Generated Captions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandAqua
                )
                Text(
                    text = if (captions.isNotEmpty()) "${captions.size} options ready to use" else "Your generated variations",
                    fontSize = 12.sp,
                    color = BrandSubtleText
                )
            }

            // Mode badge
            if (captions.isNotEmpty() || isGenerating) {
                Surface(
                    color = if (isLastAi) BrandAquaContainer else BrandDarkSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (isLastAi) BrandAqua else BrandBorder)
                ) {
                    Text(
                        text = if (isLastAi) "✨ Gemini AI" else "⚡ Smart Templates",
                        color = if (isLastAi) BrandAqua else BrandLightGrey,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Info message (shows note about AI vs template)
        generationMessage?.let { msg ->
            Surface(
                color = BrandDarkCard,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BrandBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = msg,
                    fontSize = 12.sp,
                    color = BrandSubtleText,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Content
        if (isGenerating) {
            // Loading State
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    CircularProgressIndicator(
                        color = BrandAqua,
                        strokeWidth = 3.dp,
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("loading_indicator")
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Crafting engaging captions...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandLightGrey
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Analyzing your topic, tone, and platform trends",
                        fontSize = 13.sp,
                        color = BrandSubtleText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else if (captions.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = BrandDarkCard,
                        border = BorderStroke(1.dp, BrandBorder),
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = BrandAqua,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "No Captions Generated Yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandLightGrey
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Go to the Home tab, type a short description of your photo or video, and hit Generate Captions!",
                        fontSize = 14.sp,
                        color = BrandSubtleText,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.navigateTo(Screen.HOME) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandAqua,
                            contentColor = BrandDarkCharcoal
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("button_go_to_home")
                    ) {
                        Text(
                            text = "Start Creating on Home",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // Captions list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("captions_list"),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(captions, key = { it.id }) { caption ->
                    CaptionCard(
                        caption = caption,
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onEdit = { viewModel.openEditDialog(it) },
                        onFeedback = { viewModel.showFeedback(it) }
                    )
                }

                item {
                    // Quick Action Buttons: Regenerate & More Variations
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.regenerateCaptions() },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("button_regenerate"),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, BrandAqua),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = BrandAqua
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Regenerate")
                        }

                        Button(
                            onClick = { viewModel.generateMoreVariations() },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("button_more_variations"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandDarkSurface,
                                contentColor = BrandLightGrey
                            ),
                            border = BorderStroke(1.dp, BrandBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("+2 More")
                        }
                    }
                }
            }
        }
    }
}
