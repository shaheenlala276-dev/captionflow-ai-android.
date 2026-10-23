package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CaptionItem
import com.example.ui.theme.BrandAqua
import com.example.ui.theme.BrandAquaContainer
import com.example.ui.theme.BrandBorder
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandDarkCharcoal
import com.example.ui.theme.BrandDarkSurface
import com.example.ui.theme.BrandLightGrey
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPinkContainer
import com.example.ui.theme.BrandSubtleText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CaptionCard(
    caption: CaptionItem,
    onToggleFavorite: (CaptionItem) -> Unit,
    onEdit: (CaptionItem) -> Unit,
    onDelete: ((String) -> Unit)? = null,
    onFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val heartScale by animateFloatAsState(
        targetValue = if (caption.isFavorite) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "heart_scale"
    )

    val heartColor by animateColorAsState(
        targetValue = if (caption.isFavorite) BrandPink else BrandSubtleText,
        label = "heart_color"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("caption_card_${caption.id.take(6)}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = BrandDarkCard
        ),
        border = BorderStroke(1.dp, BrandBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Platform Badge, Tone, Category & Generator indicator
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Platform badge
                Surface(
                    color = BrandAquaContainer,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BrandAqua.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = caption.platform,
                        color = BrandAqua,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Tone badge
                Surface(
                    color = BrandDarkSurface,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BrandBorder)
                ) {
                    Text(
                        text = caption.tone,
                        color = BrandLightGrey,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Category badge
                if (caption.category.isNotBlank()) {
                    Surface(
                        color = BrandDarkSurface,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, BrandBorder)
                    ) {
                        Text(
                            text = caption.category,
                            color = BrandSubtleText,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Source badge (AI vs Template)
                Surface(
                    color = if (caption.isAiGenerated) BrandAquaContainer else BrandDarkSurface,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.5.dp, if (caption.isAiGenerated) BrandAqua else BrandBorder)
                ) {
                    Text(
                        text = if (caption.isAiGenerated) "✨ AI" else "⚡ Template",
                        color = if (caption.isAiGenerated) BrandAqua else BrandSubtleText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Caption Text
            SelectionContainer {
                Text(
                    text = caption.text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = BrandLightGrey,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Divider line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BrandBorder.copy(alpha = 0.5f))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Actions row: Copy, Favorite, Edit, Share, Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Copy button
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Caption", caption.text)
                            clipboard.setPrimaryClip(clip)
                            onFeedback("Caption copied to clipboard!")
                        },
                        modifier = Modifier.testTag("button_copy")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Caption",
                            tint = BrandAqua
                        )
                    }

                    // Edit button
                    IconButton(
                        onClick = { onEdit(caption) },
                        modifier = Modifier.testTag("button_edit")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Caption",
                            tint = BrandLightGrey
                        )
                    }

                    // Share button
                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, caption.text)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share caption to:")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier.testTag("button_share")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Caption",
                            tint = BrandLightGrey
                        )
                    }

                    // Delete button if supplied
                    if (onDelete != null) {
                        IconButton(
                            onClick = { onDelete(caption.id) },
                            modifier = Modifier.testTag("button_delete")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete Caption",
                                tint = BrandSubtleText
                            )
                        }
                    }
                }

                // Favorite / Heart button
                IconButton(
                    onClick = { onToggleFavorite(caption) },
                    modifier = Modifier
                        .scale(heartScale)
                        .testTag("button_favorite")
                ) {
                    Icon(
                        imageVector = if (caption.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (caption.isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = heartColor
                    )
                }
            }
        }
    }
}
