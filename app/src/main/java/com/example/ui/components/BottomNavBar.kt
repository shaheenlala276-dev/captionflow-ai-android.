package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.BrandAqua
import com.example.ui.theme.BrandAquaContainer
import com.example.ui.theme.BrandDarkCharcoal
import com.example.ui.theme.BrandDarkSurface
import com.example.ui.theme.BrandLightGrey
import com.example.ui.theme.BrandSubtleText

@Composable
fun CaptionFlowBottomNavBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("bottom_nav_bar"),
        containerColor = BrandDarkSurface,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets(0.dp)
    ) {
        // Tab 1: Home (House)
        NavigationBarItem(
            selected = currentScreen == Screen.HOME,
            onClick = { onNavigate(Screen.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == Screen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontSize = 12.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandDarkCharcoal,
                selectedTextColor = BrandAqua,
                indicatorColor = BrandAqua,
                unselectedIconColor = BrandSubtleText,
                unselectedTextColor = BrandSubtleText
            ),
            modifier = Modifier.testTag("tab_home")
        )

        // Tab 2: Create (Sparkles)
        NavigationBarItem(
            selected = currentScreen == Screen.CREATE,
            onClick = { onNavigate(Screen.CREATE) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == Screen.CREATE) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                    contentDescription = "Create"
                )
            },
            label = {
                Text(
                    text = "Create",
                    fontSize = 12.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandDarkCharcoal,
                selectedTextColor = BrandAqua,
                indicatorColor = BrandAqua,
                unselectedIconColor = BrandSubtleText,
                unselectedTextColor = BrandSubtleText
            ),
            modifier = Modifier.testTag("tab_create")
        )

        // Tab 3: Saved (Bookmark)
        NavigationBarItem(
            selected = currentScreen == Screen.SAVED,
            onClick = { onNavigate(Screen.SAVED) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == Screen.SAVED) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Saved"
                )
            },
            label = {
                Text(
                    text = "Saved",
                    fontSize = 12.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandDarkCharcoal,
                selectedTextColor = BrandAqua,
                indicatorColor = BrandAqua,
                unselectedIconColor = BrandSubtleText,
                unselectedTextColor = BrandSubtleText
            ),
            modifier = Modifier.testTag("tab_saved")
        )
    }
}
