package app.shamilton.sigmonled.ui.picker

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import app.shamilton.sigmonled.core.color.Color
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker

@Composable
fun ColorPicker(modifier: Modifier = Modifier, color: Color = Color.BLACK, onColorChanged: (Color) -> Unit) {
    val viewModel = viewModel<ColorPresetsModel>().load(LocalContext.current)
    var hexInput by rememberSaveable { mutableStateOf(color.hex.toString()) }

    fun setColor(color: Color) {
        hexInput = color.hex.toString()
        onColorChanged(color)
    }

    // Default colors
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        viewModel.defaultColors.forEach { color ->
            Button(
                onClick = { setColor(color) },
                colors = ButtonDefaults.buttonColors(backgroundColor = color.toAndroidColor()),
            ) {}
        }
    }

    // Saved colors
    val currentContext = LocalContext.current
    var editingColors by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        viewModel.savedColors.forEach { color ->
            Button(
                onClick = {
                    if(editingColors) {
                        viewModel.savedColors.remove(color)
                        viewModel.save(currentContext)

                        if(viewModel.savedColors.isEmpty())
                            editingColors = false
                    } else {
                        setColor(color)
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = color.toAndroidColor()),
            ) {
                if(editingColors)
                    Icon(Icons.Rounded.DeleteForever, "Delete")
            }
        }
    }
    Row() {
        Button(
            onClick = {
                viewModel.savedColors.add(color)
                viewModel.save(currentContext)
            },
            enabled = !viewModel.savedColors.contains(color)
                    && !viewModel.defaultColors.contains(color),
        ) {
            Text("Add Current Color")
        }
        if(viewModel.savedColors.isNotEmpty()) {
            Button(onClick = { editingColors = !editingColors }) {
                if (editingColors)
                    Text("Stop Editing")
                else
                    Text("Edit")
            }
        }
    }

    var hexInputError by rememberSaveable { mutableStateOf(false) }
    TextField(
        label = { Text("HEX Code") },
        value = hexInput,
        onValueChange = { value ->
            hexInput = if(!value.startsWith("#"))
                "#$value"
            else
                value

            try {
                onColorChanged(Color(hexInput))
                hexInputError = false
            } catch(iae: IllegalArgumentException) {
                hexInputError = true
            }
        },
        isError = hexInputError
    )

    HarmonyColorPicker(
        modifier = modifier,
        harmonyMode = ColorHarmonyMode.NONE,
        color = color.hsv.toGoDaddyHSV(),
        onColorChanged = { hsvColor ->
            setColor(Color(hsvColor))
        }
    )
    // TODO: Make an output display
}
