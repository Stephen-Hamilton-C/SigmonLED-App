package app.shamilton.sigmonled.ui.pages.palette

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.palette.DefaultPalette
import app.shamilton.sigmonled.core.palette.PaletteConfig

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PalettePage(modifier: Modifier, commander: ArduinoCommander) {

    Column(modifier = modifier.padding(24.dp)) {
        var expanded by remember { mutableStateOf(false) }
        var selectedPalette by remember { mutableStateOf(DefaultPalette.RAINBOW)}
        var linearBlending by remember { mutableStateOf(true) }
        var solidPalette by remember { mutableStateOf(false) }

        fun apply() {
            commander.setPalette(selectedPalette, PaletteConfig(linearBlending, solidPalette))
        }

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                readOnly = true,
                value = selectedPalette.name,
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
                        Text(it.name)
                    }
                }
            }
        }

        Text("Linear Blending:")
        Switch(checked = linearBlending, onCheckedChange = {
            linearBlending = it
            apply()
        })
        Text("Solid Palette:")
        Switch(checked = solidPalette, onCheckedChange = {
            solidPalette = it
            apply()
        })

        Button(
            onClick = { apply() }
        ) {
            Text("Apply")
        }
    }
}
