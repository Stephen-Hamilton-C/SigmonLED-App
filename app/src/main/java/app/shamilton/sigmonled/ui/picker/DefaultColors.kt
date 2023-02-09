package app.shamilton.sigmonled.ui.picker

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shamilton.sigmonled.core.color.Color

val defaultColors = mutableListOf(
    Color(40.0 / 360, 200.0 / 255, 1.0),
    Color.WHITE,
    Color.RED,
    Color.ORANGE,
    Color.YELLOW,
    Color.GREEN,
    Color.BLUE,
    Color(127, 0, 255),
    Color.MAGENTA,
)

@Composable
fun DefaultColors(
    modifier: Modifier = Modifier,
    onColorSelect: (Color) -> Unit,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        defaultColors.forEach {
            ColorButton(
                onClick = { onColorSelect(it) },
                color = it,
            )
        }
    }
}
