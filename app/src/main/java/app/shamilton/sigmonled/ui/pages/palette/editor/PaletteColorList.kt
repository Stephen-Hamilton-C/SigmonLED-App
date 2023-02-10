package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.palette.Palette
import app.shamilton.sigmonled.ui.picker.ColorButton

@Composable
fun PaletteColorList(
    palette: Palette,
    onColorIndexSelected: (Int) -> Unit,
    onExpand: () -> Unit,
    onShrink: () -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(palette.colors.size) { i ->
            val color = palette.colors[i]
            ColorButton(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(6.dp)
                    .height(24.dp),
                onClick = {
                    onColorIndexSelected(i)
                },
                color = color,
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
