package app.shamilton.sigmonled.ui.picker

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shamilton.sigmonled.core.color.Color

@Composable
fun ColorButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color,
    content: @Composable () -> Unit = {},
) {
    FloatingActionButton(
        onClick = onClick,
//        colors = ButtonDefaults.buttonColors(
//            backgroundColor = color.toAndroidColor(),
//            contentColor = if(color.v >= 0.5) {
//                Color.BLACK.toAndroidColor()
//            } else {
//                Color.WHITE.toAndroidColor()
//            }
//        ),
        backgroundColor = color.toAndroidColor(),
        contentColor = if(color.v >= 0.5) {
            Color.BLACK.toAndroidColor()
        } else {
            Color.WHITE.toAndroidColor()
        },
        modifier = modifier,
        shape = CircleShape,
        content = content,
    )
}
