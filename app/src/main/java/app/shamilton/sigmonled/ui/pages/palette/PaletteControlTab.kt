package app.shamilton.sigmonled.ui.pages.palette

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.palette.DefaultPalette
import app.shamilton.sigmonled.core.palette.PaletteMode
import app.shamilton.sigmonled.ui.NumberInput
import app.shamilton.sigmonled.ui.pages.ListOption
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaletteControlTab(commander: ArduinoCommander) {
    Column(
        modifier = Modifier.padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var selectedPalette by rememberSaveable { mutableStateOf(DefaultPalette.RAINBOW) }
        var linearBlending by rememberSaveable { mutableStateOf(true) }
        var paletteMode by rememberSaveable  { mutableStateOf(PaletteMode.SCROLLING) }
        var delay by rememberSaveable  { mutableStateOf(10) }
        var stretch by rememberSaveable  { mutableStateOf(3) }
        var brightness by rememberSaveable  { mutableStateOf(255) }

        fun apply() {
            commander.setBlending(linearBlending)
            commander.setBrightness(brightness)
            commander.setDelay(delay)
            commander.setPalette(selectedPalette)
            commander.setPaletteMode(paletteMode)
            commander.setStretch(stretch)
        }

        // Palette select
        ListOption(
            label = { Text("Palette") },
            value = selectedPalette.displayName,
            onValueChange = { selectedPalette = DefaultPalette.fromDisplayName(it) },
            possibleValues = DefaultPalette.values().map { it.displayName },
        )
        Divider()

        // Palette mode select
        ListOption(
            label = { Text("Palette Mode") },
            value = paletteMode.displayName,
            onValueChange = { paletteMode = PaletteMode.fromDisplayName(it) },
            possibleValues = PaletteMode.values().map { it.displayName },
        )
        Divider()

        ListItem(
            text = { Text("Linear Blending") },
            trailing = {
                Switch(
                    checked = linearBlending,
                    onCheckedChange = null,
                )
            },
            modifier = Modifier.toggleable(
                role = Role.Switch,
                value = linearBlending,
                onValueChange = { linearBlending = it }
            )
        )
        Divider()

        // Delay input
        NumberInput(
            label = "Delay",
            value = delay,
            onValueChanged = {
                delay = it
                commander.setDelay(delay)
            },
            incrementDelta = 5,
            range = 0..4095,
            modifier = Modifier.padding(vertical = 6.dp),
        )
        Divider()

        // Stretch input
        NumberInput(
            label = "Stretch",
            value = stretch,
            onValueChanged = {
                stretch = it
                commander.setStretch(stretch)
            },
            incrementDelta = 1,
            range = 1..255,
            modifier = Modifier.padding(vertical = 6.dp),
        )
        Divider()

        // Brightness slider
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Brightness")
            Slider(
                value = brightness.toFloat(),
                onValueChange = {
                    brightness = it.roundToInt()
                    commander.setBrightness(brightness)
                },
                valueRange = 0f..255f,
            )
        }
        Divider()

        Button(
            onClick = { apply() }
        ) {
            Text("Apply")
        }
    }
}
