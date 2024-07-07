package com.looker.notesy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.looker.notesy.ui.components.NotesyTopAppBar
import com.looker.notesy.ui.navigation.NotesyNavHost
import com.looker.notesy.ui.theme.NotesyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            NotesyTheme {
                Notesy()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notesy() {
    val appState = rememberNotesyAppState()
    val topAppBarScrollBehavior = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarScrollBehavior)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AnimatedVisibility(
                visible = appState.isTopLevelDestination,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                val title = appState.currentTopLevelDestination
                    ?.title
                    ?.let { stringResource(it) }
                    ?: ""
                NotesyTopAppBar(
                    title = title,
                    scrollBehavior = scrollBehavior
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = appState.isTopLevelDestination,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                NavigationBar(
                    modifier = Modifier.navigationBarsPadding(),
                ) {
                    appState.topLevelDestinations.forEach { destination ->
                        NavigationBarItem(
                            selected = destination == appState.currentTopLevelDestination,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            icon = {
                                Icon(imageVector = destination.icon, contentDescription = null)
                            }
                        )
                    }
                }
            }
        },
        contentWindowInsets = if (appState.isTopLevelDestination) WindowInsets.systemBars
        else WindowInsets(0),
    ) {
        NotesyNavHost(
            modifier = Modifier
                .padding(it)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            appState = appState
        )
    }
}