package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.core.palette.Palette
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor

// TODO: Appears we need a PaletteModel...

@Composable
fun PaletteEditor(viewModel: PaletteEditorTabModel) {
    viewModel.selectedPalette?.let { selectedPalette ->
        Text("Editing ${selectedPalette.name}")
        Button(onClick = { viewModel.selectedPalette = null }) {
            Text("Exit")
        }
    }
}

@Composable
fun PaletteColorList(palette: Palette) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
        items(palette.colors) { color ->
            Button(onClick = {
                // Edit this color
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = color.toAndroidColor()),
            ) {}
        }
    }
}

@Composable
fun PaletteColorPicker(defaultColor: Color = Color.BLACK, onColorChanged: (Color) -> Unit) {
    ClassicColorPicker(
        showAlphaBar = false,
        color = defaultColor.hsv.toGoDaddyHSV(),
        onColorChanged = { color: HsvColor ->
            onColorChanged(Color(color))
        }
    )
}
