package app.shamilton.sigmonled.ui.pages.palette

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.palette.DefaultPalette
import app.shamilton.sigmonled.core.palette.PaletteConfig
import app.shamilton.sigmonled.ui.NumberInput
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaletteControlTab(commander: ArduinoCommander) {
    Column(modifier = Modifier.padding(24.dp)) {
        var expanded by remember { mutableStateOf(false) }
        var selectedPalette by remember { mutableStateOf(DefaultPalette.RAINBOW) }
        var linearBlending by remember { mutableStateOf(true) }
        var solidPalette by remember { mutableStateOf(false) }
        var delay by remember { mutableStateOf(10) }
        var stretch by remember { mutableStateOf(3) }
        var brightness by remember { mutableStateOf(255) }

        fun apply() {
            commander.setPalette(selectedPalette, PaletteConfig(linearBlending, solidPalette))
            commander.setDelay(delay)
            commander.setStretch(stretch - 1)
            commander.setBrightness(brightness)
        }

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                readOnly = true,
                value = selectedPalette.displayName,
                onValueChange = { },
                label = { Text("Palette") },
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DefaultPalette.values().forEach {
                    DropdownMenuItem(onClick = {
                        selectedPalette = it
                        expanded = false
                        apply()
                    }) {
                        Text(it.displayName)
                    }
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Linear Blending:")
            Spacer(Modifier.fillMaxWidth())
            Switch(checked = linearBlending, onCheckedChange = {
                linearBlending = it
                apply()
            })
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Solid Palette:")
            Spacer(Modifier.fillMaxWidth())
            Switch(checked = solidPalette, onCheckedChange = {
                solidPalette = it
                apply()
            })
        }
        Row() {
            NumberInput(
                label = "Delay",
                value = delay,
                onValueChanged = {
                    delay = it
                    commander.setDelay(delay)
                },
                incrementDelta = 5,
                range = 0..4095
            )
        }
        Row() {
            NumberInput(
                label = "Stretch",
                value = stretch,
                onValueChanged = {
                    stretch = it
                    commander.setStretch(stretch - 1)
                },
                incrementDelta = 1,
                range = 1..16
            )
        }

        // Brightness slider
        Text("Brightness")
        Slider(
            value = brightness.toFloat(),
            onValueChange = {
                brightness = it.roundToInt()
                commander.setBrightness(brightness)
            },
            valueRange = 0f..255f,
        )

        Button(
            onClick = { apply() }
        ) {
            Text("Apply")
        }
    }
}
