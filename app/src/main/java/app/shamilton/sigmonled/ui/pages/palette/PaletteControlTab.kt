package app.shamilton.sigmonled.ui.pages.palette

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.palette.DefaultPalette
import app.shamilton.sigmonled.core.palette.PaletteMode
import app.shamilton.sigmonled.ui.NumberInput
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaletteControlTab(commander: ArduinoCommander) {
    Column(modifier = Modifier.padding(24.dp)) {
        var selectedPaletteExpanded by remember { mutableStateOf(false) }
        var selectedPalette by remember { mutableStateOf(DefaultPalette.RAINBOW) }
        var linearBlending by remember { mutableStateOf(true) }
        var paletteModeExpanded by remember { mutableStateOf(false) }
        var paletteMode by remember { mutableStateOf(PaletteMode.SCROLLING) }
        var delay by remember { mutableStateOf(10) }
        var stretch by remember { mutableStateOf(3) }
        var brightness by remember { mutableStateOf(255) }

        fun apply() {
            commander.setBlending(linearBlending)
            commander.setBrightness(brightness)
            commander.setDelay(delay)
            commander.setPalette(selectedPalette)
            commander.setPaletteMode(paletteMode)
            commander.setStretch(stretch)
        }

        ExposedDropdownMenuBox(
            expanded = selectedPaletteExpanded,
            onExpandedChange = { selectedPaletteExpanded = !selectedPaletteExpanded },
        ) {
            TextField(
                readOnly = true,
                value = selectedPalette.displayName,
                onValueChange = { },
                label = { Text("Palette") },
            )
            ExposedDropdownMenu(
                expanded = selectedPaletteExpanded,
                onDismissRequest = { selectedPaletteExpanded = false },
            ) {
                DefaultPalette.values().forEach {
                    DropdownMenuItem(onClick = {
                        selectedPalette = it
                        selectedPaletteExpanded = false
                        commander.setPalette(selectedPalette)
                    }) {
                        Text(it.displayName)
                    }
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = paletteModeExpanded,
            onExpandedChange = { paletteModeExpanded = !paletteModeExpanded },
        ) {
            TextField(
                readOnly = true,
                value = paletteMode.displayName,
                onValueChange = { },
                label = { Text("Palette Mode") },
            )
            ExposedDropdownMenu(
                expanded = paletteModeExpanded,
                onDismissRequest = { paletteModeExpanded = false },
            ) {
                PaletteMode.values().forEach {
                    DropdownMenuItem(onClick = {
                        paletteMode = it
                        paletteModeExpanded = false
                        commander.setPaletteMode(paletteMode)
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
                commander.setBlending(linearBlending)
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
                    commander.setStretch(stretch)
                },
                incrementDelta = 1,
                range = 1..255
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
