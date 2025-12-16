package pdm.unindubria.stormbringer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = stormbringer_primary,
    secondary = stormbringer_primary_dark,
    tertiary = stormbringer_primary,
    background = stormbringer_background_dark,
    surface = stormbringer_surface_dark,
    onPrimary = stormbringer_background_dark,
    onSecondary = stormbringer_background_dark,
    onTertiary = stormbringer_background_dark,
    onBackground = white_90,
    onSurface = white_90,
)

@Composable
fun StormbringerTheme(
    content: @Composable () -> Unit
) {


    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}