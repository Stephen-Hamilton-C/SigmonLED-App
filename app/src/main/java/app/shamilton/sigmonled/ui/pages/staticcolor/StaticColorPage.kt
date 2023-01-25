package app.shamilton.sigmonled.ui.pages.staticcolor

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.ui.ColorPicker
import kotlin.concurrent.timer

var colorChanged = false

@Composable
fun StaticColorPage(modifier: Modifier, commander: ArduinoCommander) {
    var currentColor by remember { mutableStateOf(Color(255, 255, 255)) }
    var hexInput by remember { mutableStateOf(currentColor.hex.toString()) }
    var timerCreated by remember { mutableStateOf(false) }
    val viewModel = viewModel<ColorPresetsModel>().load(LocalContext.current)

    fun setColor(color: Color) {
        currentColor = color
        hexInput = color.hex.toString()
    }

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
    ) {

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
        var editingColors by remember { mutableStateOf(false) }
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
            Button(onClick = {
                viewModel.savedColors.add(currentColor)
                viewModel.save(currentContext)
            }) {
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

        var hexInputError by remember { mutableStateOf(false) }
        TextField(
            label = { Text("HEX Code") },
            value = hexInput,
            onValueChange = { value ->
                hexInput = if(!value.startsWith("#"))
                    "#$value"
                else
                    value

                try {
                    currentColor = Color(hexInput)
                    hexInputError = false
                } catch(iae: IllegalArgumentException) {
                    hexInputError = true
                }
            },
            isError = hexInputError
        )

        // Color picker
        ColorPicker(
            color = currentColor,
            onColorChanged = { color ->
                colorChanged = true
                setColor(color)
            }
        )
    }
}
