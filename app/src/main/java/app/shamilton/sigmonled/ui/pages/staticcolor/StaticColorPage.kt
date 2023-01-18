package app.shamilton.sigmonled.ui.pages.staticcolor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.color.Color
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor

@Composable
fun StaticColorPage(modifier: Modifier, commander: ArduinoCommander) {
    var currentColor = Color(255, 255, 255)

    Column(modifier = modifier.padding(24.dp)) {
        ClassicColorPicker(
            showAlphaBar = false,
            color = currentColor.hsv.toGoDaddyHSV(),
            onColorChanged = { color: HsvColor ->
                currentColor = Color(color)
                commander.setColor(currentColor)
            }
        )
    }
}
