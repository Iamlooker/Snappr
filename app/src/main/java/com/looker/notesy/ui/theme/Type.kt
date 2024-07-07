package com.looker.notesy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.looker.notesy.R

val bodyFontFamily: FontFamily
    get() = FontFamily(
        Font(R.font.poppins_medium, weight = FontWeight.Medium),
        Font(R.font.poppins_regular, weight = FontWeight.Normal),
    )

val displayFontFamily: FontFamily
    get() = FontFamily(
        Font(R.font.darkergrotesque_black, weight = FontWeight.Black),
        Font(R.font.darkergrotesque_bold, weight = FontWeight.Bold),
        Font(R.font.darkergrotesque_semibold, weight = FontWeight.SemiBold),
        Font(R.font.darkergrotesque_medium, weight = FontWeight.Medium),
        Font(R.font.darkergrotesque_regular, weight = FontWeight.Normal),
    )

val baseline = Typography()

val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.Black,
    ),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    displaySmall = baseline.displaySmall.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineLarge = baseline.headlineLarge.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    headlineMedium = baseline.headlineMedium.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.Medium,
    ),
    titleLarge = baseline.titleLarge.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    titleSmall = baseline.titleSmall.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.Medium,
    ),
    bodyLarge = baseline.bodyLarge.copy(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = baseline.bodyMedium.copy(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    bodySmall = baseline.bodySmall.copy(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    labelLarge = baseline.labelLarge.copy(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Medium,
    ),
    labelMedium = baseline.labelMedium.copy(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Medium,
    ),
    labelSmall = baseline.labelSmall.copy(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Medium,
    ),
)