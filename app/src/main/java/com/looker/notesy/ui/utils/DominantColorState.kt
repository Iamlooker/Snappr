package com.looker.notesy.ui.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberDominantColorState(
	context: Context = LocalContext.current,
	defaultColor: Color = MaterialTheme.colorScheme.surface,
	cacheSize: Int = 12,
): DominantColorState = remember { DominantColorState(context, defaultColor, cacheSize) }

@Stable
class DominantColorState(
	private val context: Context,
	private val defaultColor: Color,
	cacheSize: Int = 50,
) {
	var color by mutableStateOf(defaultColor)
		private set

	private val cache = when {
		cacheSize > 0 -> LruCache<String, DominantColors>(cacheSize)
		else -> null
	}

	suspend fun updateColorsFromImageUrl(url: String?) {
		val result = calculateDominantColorFromUrl(url)
		color = result?.color ?: defaultColor
	}

	private suspend fun calculateDominantColorFromUrl(url: String?): DominantColors? {
		return if (url != null) {
			cache?.get(url)
				?: context.calculateColorFromImageUrl(url)?.let { dominantColor ->
					DominantColors(dominantColor).also { result -> cache?.put(url, result) }
				}
		} else null
	}
}

@Immutable
data class DominantColors(val color: Color = Color.Unspecified)

suspend fun Context.calculateColorFromImageUrl(
	imageUrl: String,
):Color? = imageUrl.bitmap(this)?.let {
	if (it.getVibrantColor() == Color(0)) it.getDominantColor()
	else it.getVibrantColor()
}

suspend fun String.bitmap(context: Context): Bitmap? = withContext(Dispatchers.IO) {
	val imageRequest = ImageRequest.Builder(context)
		.data(this@bitmap)
		.size(128)
		.scale(Scale.FILL)
		.allowHardware(false)
		.build()

	when (val result = imageRequest.context.imageLoader.execute(imageRequest)) {
		is SuccessResult -> result.drawable.toBitmap()
		is ErrorResult -> null
	}
}

suspend fun Bitmap.getVibrantColor(): Color = withContext(Dispatchers.IO) {
	this@getVibrantColor.let {
		Palette.Builder(it)
			.resizeBitmapArea(0)
			.clearFilters()
			.maximumColorCount(8)
			.generate()
	}.getVibrantColor(0).toComposeColor()
}

suspend fun Bitmap.getDominantColor(): Color = withContext(Dispatchers.IO) {
	this@getDominantColor.let {
		Palette.Builder(it)
			.resizeBitmapArea(0)
			.clearFilters()
			.maximumColorCount(8)
			.generate()
	}.getDominantColor(0).toComposeColor()
}

fun Int.toComposeColor() = Color(this)