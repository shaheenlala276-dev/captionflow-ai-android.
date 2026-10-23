package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QuickCategory
import com.example.model.SocialPlatform
import com.example.ui.MainViewModel
import com.example.ui.SavedTab
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
import com.example.ui.theme.BrandPinkContainer
import com.example.ui.theme.BrandSubtleText

@Composable
fun SavedScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val savedTab by viewModel.savedTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filterPlatform by viewModel.filterPlatform.collectAsState()
    val filterCategory by viewModel.filterCategory.collectAsState()
    val showClearDialog by viewModel.showClearHistoryDialog.collectAsState()
    val favoritesList by viewModel.favoritesList.collectAsState()
    val historyList by viewModel.historyList.collectAsState()
    val editingCaption by viewModel.editingCaption.collectAsState()

    // Dialog for editing
    editingCaption?.let { item ->
        EditCaptionDialog(
            caption = item,
            onDismiss = { viewModel.closeEditDialog() },
            onSave = { newText -> viewModel.saveEditedCaption(newText) }
        )
    }

    // Confirmation dialog before clearing history
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowClearHistoryDialog(false) },
            containerColor = BrandDarkCard,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = "Clear All History?",
                    color = BrandLightGrey,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "This will remove all saved history entries permanently. Your favorites will still be kept.",
                    color = BrandSubtleText,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmClearHistory() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandPink,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("confirm_clear_history_button")
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.setShowClearHistoryDialog(false) }
                ) {
                    Text("Cancel", color = BrandLightGrey)
                }
            }
        )
    }

    val baseList = if (savedTab == SavedTab.FAVORITES) favoritesList else historyList

    // Filter by search, platform, category
    val filteredList = remember(baseList, searchQuery, filterPlatform, filterCategory) {
        baseList.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                item.text.contains(searchQuery, ignoreCase = true) ||
                item.category.contains(searchQuery, ignoreCase = true) ||
                item.platform.contains(searchQuery, ignoreCase = true)

            val matchesPlatform = filterPlatform == "All" ||
                item.platform.equals(filterPlatform, ignoreCase = true)

            val matchesCategory = filterCategory == "All" ||
                item.category.equals(filterCategory, ignoreCase = true)

            matchesSearch && matchesPlatform && matchesCategory
        }
    }

    val platforms = listOf("All") + SocialPlatform.values().map { it.displayName }
    val categories = listOf("All") + QuickCategory.values().map { it.displayName }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandDarkCharcoal)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("screen_saved")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Saved Captions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandAqua
                )
                Text(
                    text = "Manage your favorites and history library",
                    fontSize = 12.sp,
                    color = BrandSubtleText
                )
            }

            if (savedTab == SavedTab.HISTORY && historyList.isNotEmpty()) {
                OutlinedButton(
                    onClick = { viewModel.setShowClearHistoryDialog(true) },
                    border = BorderStroke(1.dp, BrandPink.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPink),
                    modifier = Modifier.testTag("button_clear_history")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Clear",
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs: Favorites & History
        TabRow(
            selectedTabIndex = if (savedTab == SavedTab.FAVORITES) 0 else 1,
            containerColor = BrandDarkCard,
            contentColor = BrandAqua,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[if (savedTab == SavedTab.FAVORITES) 0 else 1]),
                    color = if (savedTab == SavedTab.FAVORITES) BrandPink else BrandAqua,
                    height = 3.dp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandDarkCard, RoundedCornerShape(14.dp))
        ) {
            Tab(
                selected = savedTab == SavedTab.FAVORITES,
                onClick = { viewModel.setSavedTab(SavedTab.FAVORITES) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = if (savedTab == SavedTab.FAVORITES) BrandPink else BrandSubtleText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Favorites (${favoritesList.size})",
                            color = if (savedTab == SavedTab.FAVORITES) BrandPink else BrandSubtleText,
                            fontWeight = if (savedTab == SavedTab.FAVORITES) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                },
                modifier = Modifier.testTag("tab_favorites")
            )

            Tab(
                selected = savedTab == SavedTab.HISTORY,
                onClick = { viewModel.setSavedTab(SavedTab.HISTORY) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = if (savedTab == SavedTab.HISTORY) BrandAqua else BrandSubtleText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "History (${historyList.size})",
                            color = if (savedTab == SavedTab.HISTORY) BrandAqua else BrandSubtleText,
                            fontWeight = if (savedTab == SavedTab.HISTORY) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                },
                modifier = Modifier.testTag("tab_history")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            placeholder = {
                Text(
                    text = "Search by keyword or category...",
                    fontSize = 13.sp,
                    color = BrandSubtleText
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = BrandSubtleText
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = BrandSubtleText
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_search_saved"),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = BrandLightGrey,
                unfocusedTextColor = BrandLightGrey,
                focusedBorderColor = BrandAqua,
                unfocusedBorderColor = BrandBorder,
                focusedContainerColor = BrandDarkCard,
                unfocusedContainerColor = BrandDarkCard,
                cursorColor = BrandAqua
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Platform Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            platforms.forEach { platform ->
                val isSelected = filterPlatform == platform
                Surface(
                    modifier = Modifier
                        .clickable { viewModel.setFilterPlatform(platform) }
                        .testTag("filter_platform_$platform"),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) BrandAquaContainer else BrandDarkCard,
                    border = BorderStroke(1.dp, if (isSelected) BrandAqua else BrandBorder)
                ) {
                    Text(
                        text = platform,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) BrandAqua else BrandLightGrey,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = filterCategory == cat
                Surface(
                    modifier = Modifier
                        .clickable { viewModel.setFilterCategory(cat) }
                        .testTag("filter_category_$cat"),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) BrandPinkContainer else BrandDarkCard,
                    border = BorderStroke(1.dp, if (isSelected) BrandPink else BrandBorder)
                ) {
                    Text(
                        text = cat,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) BrandPink else BrandLightGrey,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // List or Empty State
        if (filteredList.isEmpty()) {
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
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (savedTab == SavedTab.FAVORITES) Icons.Default.Favorite else Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = if (savedTab == SavedTab.FAVORITES) BrandPink else BrandAqua,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (savedTab == SavedTab.FAVORITES) "No Favorites Yet" else "No History Yet",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandLightGrey
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (savedTab == SavedTab.FAVORITES) {
                            "Tap the pink heart icon on any generated caption to save your favorites here!"
                        } else {
                            "Every caption you generate is automatically organized and kept in your history."
                        },
                        fontSize = 13.sp,
                        color = BrandSubtleText,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("saved_captions_list"),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList, key = { it.id }) { caption ->
                    CaptionCard(
                        caption = caption,
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onEdit = { viewModel.openEditDialog(it) },
                        onDelete = { viewModel.deleteCaption(it) },
                        onFeedback = { viewModel.showFeedback(it) }
                    )
                }
            }
        }
    }
}
