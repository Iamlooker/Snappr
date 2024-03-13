package com.looker.notesy.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.SubcomposeAsyncImageScope

@Composable
fun NotesyImage(
    data: Any?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null
) {
    AsyncImage(
        modifier = modifier,
        model = data,
        alignment = alignment,
        contentScale = contentScale,
        contentDescription = contentDescription
    )
}

@Composable
fun NotesyImageWithAmbience(
    data: Any?,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    ambienceOptions: AmbienceOptions = AmbienceOptions(),
    ambientImage: @Composable SubcomposeAsyncImageScope.() -> Unit = {
        DefaultAmbientImage(ambienceOptions, imageModifier)
    },
    contentDescription: String? = null
) {
    SubcomposeAsyncImage(
        model = data,
        modifier = modifier,
        contentScale = contentScale,
        alignment = alignment,
        contentDescription = contentDescription
    ) {
        ambientImage()
        SubcomposeAsyncImageContent(imageModifier)
    }
}

@Composable
private fun SubcomposeAsyncImageScope.DefaultAmbientImage(
    ambienceOptions: AmbienceOptions,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImageContent(
        modifier = Modifier
            .scale(ambienceOptions.scaleUpRatio)
            .blur(ambienceOptions.blurRadius.dp, ambienceOptions.edgeTreatment)
            .alpha(ambienceOptions.backgroundAlpha)
            .then(modifier)
    )
}

data class AmbienceOptions(
    val backgroundAlpha: Float = 0.4f,
    val blurRadius: Int = 24,
    val edgeTreatment: BlurredEdgeTreatment = BlurredEdgeTreatment.Unbounded,
    val scaleUpRatio: Float = 2f
)