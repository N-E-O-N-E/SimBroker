package de.neone.simbroker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import de.neone.simbroker.R

/**
 * Provider für Google Fonts über den Android-Systemdienst.
 *
 * Verwendet den GMS-Fonts-Provider, um Schriftarten dynamisch zu laden.
 */
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

//==============================================================================================
// 1) Definition der FontFamilies
//==============================================================================================

/**
 * Schriftfamilie für große Display- und Überschriftstexte.
 *
 * Lädt die Google Font "Noto Sans Japanese" über den definierten [provider].
 */
val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Noto Sans Japanese"),
        fontProvider = provider,
    )
)

/**
 * Schriftfamilie für Fließtext und Labels.
 *
 * Verwendet ebenfalls die Google Font "Noto Sans Japanese".
 */
val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Noto Sans Japanese"),
        fontProvider = provider,
    )
)

//==============================================================================================
// 2) Baseline Typography
//==============================================================================================

/**
 * Standard-Material3-Typografie als Ausgangspunkt.
 *
 * Wird genutzt, um nur die FontFamily zu überschreiben, ohne weitere Stiländerungen.
 */
val baseline = Typography()

//==============================================================================================
// 3) App-spezifische Typography-Konfiguration
//==============================================================================================

/**
 * App-spezifische [Typography], die die Standard-Styles übernimmt
 * und die FontFamily für Display- und Body-Styles anpasst.
 *
 * - Display- und Headline-Styles verwenden [displayFontFamily].
 * - Body- und Label-Styles verwenden [bodyFontFamily].
 */
val AppTypography = Typography(
    // Display-Stile
    displayLarge  = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall  = baseline.displaySmall.copy(fontFamily = displayFontFamily),

    // Headline-Stile
    headlineLarge  = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall  = baseline.headlineSmall.copy(fontFamily = displayFontFamily),

    // Title-Stile
    titleLarge  = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall  = baseline.titleSmall.copy(fontFamily = displayFontFamily),

    // Body-Stile
    bodyLarge  = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall  = baseline.bodySmall.copy(fontFamily = bodyFontFamily),

    // Label-Stile
    labelLarge  = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall  = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)
