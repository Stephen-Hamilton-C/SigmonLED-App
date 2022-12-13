package app.shamilton.sigmonled.ui.pages.staticcolor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.color.Color

@Composable
fun StaticColor(modifier: Modifier, commander: ArduinoCommander) {
    var hue by remember { mutableStateOf(0.0f) }
    var saturation by remember { mutableStateOf(0.0f) }
    var brightness by remember { mutableStateOf(1.0f) }

    Column(modifier = modifier.padding(24.dp)) {
        fun apply() {
            commander.setColor(
                Color(
                    hue.toDouble(),
                    saturation.toDouble(),
                    brightness.toDouble()
                )
            )
        }
        Slider(
            value = hue,
            valueRange = 0.0f..1.0f,
            onValueChange = {
                hue = it
                apply()
            }
        )
        Slider(
            value = saturation,
            valueRange = 0.0f..1.0f,
            onValueChange = {
                saturation = it
                apply()
            }
        )
        Slider(
            value = brightness,
            valueRange = 0.0f..1.0f,
            onValueChange = {
                brightness = it
                apply()
            }
        )
        Button(onClick = {
            apply()
        }) {
            Text("Apply")
        }
    }
}
