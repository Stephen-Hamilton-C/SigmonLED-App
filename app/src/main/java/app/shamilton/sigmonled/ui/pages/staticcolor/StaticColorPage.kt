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
import kotlin.concurrent.timer

var currentColor = Color(255, 255, 255)
var colorChanged = false
var timerCreated = false

@Composable
fun StaticColorPage(modifier: Modifier, commander: ArduinoCommander) {
    if(!timerCreated) {
        timerCreated = true
        timer(period = 10L) {
            if(colorChanged) {
                colorChanged = false
                commander.setColor(currentColor)
            }
        }
    }

    Column(modifier = modifier.padding(24.dp).padding(bottom = 132.dp)) {
        ClassicColorPicker(
            showAlphaBar = false,
            color = currentColor.hsv.toGoDaddyHSV(),
            onColorChanged = { color: HsvColor ->
                colorChanged = true
                currentColor = Color(color)
            }
        )
        // TODO: Add row of common colors
        // TODO: Make a user-defined color palette
    }
}
