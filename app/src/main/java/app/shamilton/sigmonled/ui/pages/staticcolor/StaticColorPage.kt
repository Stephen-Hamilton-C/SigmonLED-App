package app.shamilton.sigmonled.ui.pages.staticcolor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.ui.ColorPicker
import kotlin.concurrent.timer

var colorChanged = false

@Composable
fun StaticColorPage(modifier: Modifier, commander: ArduinoCommander) {
    var currentColor by remember { mutableStateOf(Color(255, 255, 255)) }
    var timerCreated by remember { mutableStateOf(false) }

    if(!timerCreated) {
        timerCreated = true
        timer(period = 10L) {
            if(colorChanged) {
                colorChanged = false
                commander.setColor(currentColor)
            }
        }
    }

    Column(modifier = modifier
        .padding(24.dp)
        .padding(bottom = 132.dp)) {
        Button(onClick = {
            colorChanged = true
            currentColor = Color(40.0 / 360, 200.0 / 255, 1.0)
            println("Changed color to $currentColor")
        }) {
            Text("Warm")
        }
        ColorPicker(
            color = currentColor,
            onColorChanged = { color ->
                colorChanged = true
                currentColor = color
            }
        )
    }
}
