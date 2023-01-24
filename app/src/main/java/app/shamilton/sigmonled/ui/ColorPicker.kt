package app.shamilton.sigmonled.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shamilton.sigmonled.core.color.Color
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker

@Composable
fun ColorPicker(modifier: Modifier = Modifier, color: Color = Color.BLACK, onColorChanged: (Color) -> Unit) {
    HarmonyColorPicker(
        modifier = modifier,
        harmonyMode = ColorHarmonyMode.NONE,
        color = color.hsv.toGoDaddyHSV(),
        onColorChanged = { hsvColor ->
            onColorChanged(Color(hsvColor))
        }
    )
    // TODO: Make HEX input
    // TODO: Make an output display
    // TODO: Add row of common colors
    // TODO: Make a user-defined color palette
}
