package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.CaptionFlowBottomNavBar
import com.example.ui.screens.CreateScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.theme.BrandAqua
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandDarkCharcoal
import com.example.ui.theme.BrandLightGrey
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsState()
                val feedbackMessage by viewModel.feedbackMessage.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                // Show feedback in snackbar
                LaunchedEffect(feedbackMessage) {
                    feedbackMessage?.let { msg ->
                        snackbarHostState.showSnackbar(
                            message = msg,
                            duration = SnackbarDuration.Short
                        )
                        viewModel.dismissFeedback()
                    }
                }

                // Handle system back navigation to return to Home from secondary tabs
                BackHandler(enabled = currentScreen != Screen.HOME) {
                    viewModel.navigateTo(Screen.HOME)
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BrandDarkCharcoal),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        CaptionFlowBottomNavBar(
                            currentScreen = currentScreen,
                            onNavigate = { screen -> viewModel.navigateTo(screen) }
                        )
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) { data ->
                            Snackbar(
                                snackbarData = data,
                                containerColor = BrandDarkCard,
                                contentColor = BrandLightGrey,
                                actionColor = BrandAqua
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(BrandDarkCharcoal)
                    ) {
                        Crossfade(
                            targetState = currentScreen,
                            label = "screen_transition"
                        ) { screen ->
                            when (screen) {
                                Screen.HOME -> HomeScreen(viewModel = viewModel)
                                Screen.CREATE -> CreateScreen(viewModel = viewModel)
                                Screen.SAVED -> SavedScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
