package com.looker.notesy.ui.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun Context.calculateColorFromImageUrl(
    imageUrl: String,
): Color? = imageUrl.bitmap(this)?.let {
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