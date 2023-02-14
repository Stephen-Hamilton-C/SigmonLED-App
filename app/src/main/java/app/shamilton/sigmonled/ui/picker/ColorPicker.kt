package app.shamilton.sigmonled.ui.picker

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.shamilton.sigmonled.core.color.Color
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker

@Composable
fun ColorPicker(modifier: Modifier = Modifier, color: Color = Color.BLACK, onColorChanged: (Color) -> Unit) {
    Column(

    ) {
        val viewModel = viewModel<ColorPresetsModel>().load(LocalContext.current)
        var hexInput by rememberSaveable { mutableStateOf(color.hex.toString()) }

        fun setColor(color: Color) {
            hexInput = color.hex.toString()
            onColorChanged(color)
        }

        // Default colors
        DefaultColors(
            onColorSelect = { setColor(it) },
        )

        // Saved colors
        val currentContext = LocalContext.current
        var editingColors by rememberSaveable { mutableStateOf(false) }
        SavedColors(
            onColorSelect = {
                if (editingColors) {
                    viewModel.savedColors.remove(it)
                    viewModel.save(currentContext)

                    if (viewModel.savedColors.isEmpty())
                        editingColors = false
                } else {
                    setColor(it)
                }
            },
            viewModel = viewModel,
            isEditing = editingColors,
            modifier = Modifier
                .padding(vertical = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Add current color
            Button(
                onClick = {
                    viewModel.savedColors.add(color)
                    viewModel.save(currentContext)
                },
                enabled = !viewModel.savedColors.contains(color)
                        && !defaultColors.contains(color),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(end = 6.dp)
            ) {
                Text("Add Current Color")
            }

            // Edit button
            Button(
                onClick = { editingColors = !editingColors },
                enabled = viewModel.savedColors.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 6.dp)
            ) {
                if (editingColors)
                    Text("Stop Editing")
                else
                    Text("Edit")
            }
        }

        var hexInputError by rememberSaveable { mutableStateOf(false) }
        TextField(
            label = { Text("HEX Code") },
            value = hexInput,
            onValueChange = { value ->
                hexInput = if (!value.startsWith("#"))
                    "#$value"
                else
                    value

                try {
                    onColorChanged(Color(hexInput))
                    hexInputError = false
                } catch (iae: IllegalArgumentException) {
                    hexInputError = true
                }
            },
            isError = hexInputError,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 6.dp)
        )

        HarmonyColorPicker(
            modifier = modifier,
            harmonyMode = ColorHarmonyMode.NONE,
            color = color.hsv.toGoDaddyHSV(),
            onColorChanged = { hsvColor ->
                setColor(Color(hsvColor))
            }
        )
    }
}
