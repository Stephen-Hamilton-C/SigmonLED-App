package app.shamilton.sigmonled.ui.picker

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    Column(
        modifier = modifier
    ) {
        Text("Default colors:")
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(top = 6.dp)
        ) {
            defaultColors.forEach {
                val colorMod = if(it != defaultColors.last()) {
                    Modifier.padding(end = 4.dp)
                } else {
                    Modifier
                }
                ColorButton(
                    onClick = { onColorSelect(it) },
                    color = it,
                    modifier = colorMod
                )
            }
        }
    }
}
