package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.core.palette.Palette
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker

@Composable
fun PaletteEditor(viewModel: PaletteEditorTabModel) {
    viewModel.selectedPalette?.let { selectedPalette ->
        var currentColorIndex: Int by remember { mutableStateOf(-1) }
        var name: String by remember { mutableStateOf(selectedPalette.name) }
        if(currentColorIndex < 0) {
            TextField(value = name, onValueChange = { name = it })
            PaletteColorList(
                palette = selectedPalette,
                onColorIndexSelected = { currentColorIndex = it },
                onExpand = {
                    viewModel.selectedPalette = selectedPalette.expand()
                },
                onShrink = {
                    viewModel.selectedPalette = selectedPalette.shrink()
                }
            )
            val currentContext = LocalContext.current
            Button(onClick = {
                // Change name first if it has been changed
                if(selectedPalette.name != name)
                    viewModel.selectedPalette = selectedPalette.changeName(name)

                viewModel.selectedPalette?.let {
                    if (viewModel.selectedPaletteIndex in 0..viewModel.savedPalettes.size) {
                        // Save existing palette
                        viewModel.savedPalettes[viewModel.selectedPaletteIndex] = it
                    } else {
                        // Save new palette
                        viewModel.savedPalettes.add(it)
                    }
                }

                // Go back to palette list and write changes
                viewModel.selectedPalette = null
                savePalettes(currentContext, viewModel.savedPalettes)
            }) {
                Text("Save")
            }
            Button(onClick = { viewModel.selectedPalette = null }) {
                Text("Exit")
            }
        } else {
            PaletteColorPicker(
                defaultColor = selectedPalette.colors[currentColorIndex],
                onSave = { color ->
                    viewModel.selectedPalette = selectedPalette.changeColor(currentColorIndex, color)
                    currentColorIndex = -1
                },
                onExit = {
                    currentColorIndex = -1
                }
            )
        }
    }
}

@Composable
fun PaletteColorList(palette: Palette, onColorIndexSelected: (Int) -> Unit, onExpand: () -> Unit, onShrink: () -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
        items(palette.colors.size) { i ->
            val color = palette.colors[i]
            Button(onClick = {
                onColorIndexSelected(i)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = color.toAndroidColor()),
            ) {}
        }
    }

    Row() {
        Button(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(horizontal = 6.dp),
            onClick = onExpand,
            enabled = palette.canExpand,
        ) {
            Text("Expand")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(horizontal = 6.dp),
            onClick = onShrink,
            enabled = palette.canShrink,
        ) {
            Text("Shrink")
        }
    }
}

@Composable
fun PaletteColorPicker(defaultColor: Color = Color.BLACK, onSave: (Color) -> Unit, onExit: () -> Unit) {
    var currentColor: Color by remember { mutableStateOf(defaultColor) }
//    ClassicColorPicker(
//        modifier = Modifier.fillMaxHeight(0.75f).padding(12.dp),
//        showAlphaBar = false,
//        color = currentColor.hsv.toGoDaddyHSV(),
//        onColorChanged = { color: HsvColor ->
//            currentColor = Color(color)
//        }
//    )
    HarmonyColorPicker(
        modifier = Modifier.fillMaxHeight(0.75f).padding(12.dp),
        harmonyMode = ColorHarmonyMode.NONE,
        color = currentColor.hsv.toGoDaddyHSV(),
        onColorChanged = { color: HsvColor ->
            currentColor = Color(color)
        })
    Button(onClick = {
        onSave(currentColor)
    }) {
        Text("Save")
    }
    Button(onClick = onExit) {
        Text("Exit")
    }
}
