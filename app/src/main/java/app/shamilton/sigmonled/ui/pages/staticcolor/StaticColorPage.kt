package app.shamilton.sigmonled.ui.pages.staticcolor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.ui.picker.ColorPicker
import kotlin.concurrent.timer

var colorChanged = false

@Composable
fun StaticColorPage(modifier: Modifier, commander: ArduinoCommander) {
    var currentColor by rememberSaveable(stateSaver = Color.Saver) { mutableStateOf(Color(255, 255, 255)) }
    var timerCreated by rememberSaveable { mutableStateOf(false) }

    if(!timerCreated) {
        timerCreated = true
        timer(period = 10L) {
            if(colorChanged) {
                colorChanged = false
                commander.setColor(currentColor)
            }
        }
    }

    Column(
        modifier = modifier
            .padding(12.dp)
    ) {
        // Color picker
        ColorPicker(
            color = currentColor,
            onColorChanged = { color ->
                colorChanged = true
                currentColor = color
            }
        )
    }
}
