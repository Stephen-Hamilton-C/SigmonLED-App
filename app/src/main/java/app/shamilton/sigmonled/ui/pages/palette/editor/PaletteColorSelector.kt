package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.ui.picker.ColorPicker

@Composable
fun PaletteColorSelector(defaultColor: Color = Color.BLACK, onSave: (Color) -> Unit, onExit: () -> Unit) {
    var currentColor: Color by remember { mutableStateOf(defaultColor) }
    ColorPicker(
        modifier = Modifier
            .fillMaxHeight(0.75f)
            .padding(12.dp),
        color = currentColor,
        onColorChanged = { color ->
            currentColor = color
        },
    )
    Button(onClick = {
        onSave(currentColor)
    }) {
        Text("Save")
    }
    Button(onClick = onExit) {
        Text("Exit")
    }
}
