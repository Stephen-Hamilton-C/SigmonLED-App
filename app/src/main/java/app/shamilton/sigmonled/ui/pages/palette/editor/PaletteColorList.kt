package app.shamilton.sigmonled.ui.pages.palette.editor

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.palette.Palette
import app.shamilton.sigmonled.ui.picker.ColorButton

@Composable
fun PaletteColorList(
    palette: Palette,
    onColorIndexSelected: (Int) -> Unit,
) {
    val columnCount = when(LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 8
        else -> 4
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(palette.colors.size) { i ->
            val color = palette.colors[i]
            ColorButton(
                onClick = {
                    onColorIndexSelected(i)
                },
                color = color,
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(6.dp)
                    .height(12.dp)
            ) {}
        }
    }
}
