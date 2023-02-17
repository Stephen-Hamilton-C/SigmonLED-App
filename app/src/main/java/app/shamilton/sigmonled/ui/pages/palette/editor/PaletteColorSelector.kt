package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.ui.picker.ColorPicker

@Composable
fun PaletteColorSelector(defaultColor: Color = Color.BLACK, onSave: (Color) -> Unit, onExit: () -> Unit) {
    Column(
        modifier = Modifier.padding(6.dp)
    ) {
        var currentColor: Color by rememberSaveable(stateSaver = Color.Saver) { mutableStateOf(defaultColor) }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            ColorPicker(
                modifier = Modifier
                    .fillMaxHeight(0.75f)
                    .padding(12.dp),
                color = currentColor,
                onColorChanged = { color ->
                    currentColor = color
                },
            )
        }
        Row() {
            Button(
                onClick = onExit,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(end = 6.dp)
            ) {
                Text("Back")
            }
            Button(
                onClick = { onSave(currentColor) },
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 6.dp)
            ) {
                Text("Save Color")
            }
        }
    }
}
