package com.looker.notesy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.looker.notesy.ui.utils.LocalSpacing
import com.looker.notesy.ui.utils.Spacing

private val DarkColorScheme = darkColorScheme(
	primary = Purple80,
	secondary = PurpleGrey80,
	tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
	primary = Purple40,
	secondary = PurpleGrey40,
	tertiary = Pink40
)

@Composable
fun NotesyTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	dynamicColor: Boolean = false,
	content: @Composable () -> Unit
) {
	val colorScheme = when {
		dynamicColor && isDynamicThemeSupported() -> {
			val context = LocalContext.current
			if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
		}

		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}
	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			with(WindowCompat.getInsetsController(window, view)) {
				isAppearanceLightStatusBars = !darkTheme
				isAppearanceLightNavigationBars = !darkTheme
			}
		}
	}
	CompositionLocalProvider(LocalSpacing provides Spacing()) {
		MaterialTheme(
			colorScheme = colorScheme,
			typography = Typography,
			shapes = shapes,
			content = content
		)
	}
}

@Composable
@ChecksSdkIntAtLeast(31)
fun isDynamicThemeSupported(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S