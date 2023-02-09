package app.shamilton.sigmonled.ui.picker

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shamilton.sigmonled.core.color.Color

@Composable
fun SavedColors(
    modifier: Modifier = Modifier,
    onColorSelect: (Color) -> Unit,
    viewModel: ColorPresetsModel,
    isEditing: Boolean,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
    ) {
        viewModel.savedColors.forEach {
            ColorButton(
                onClick = { onColorSelect(it) },
                color = it,
            ) {
                if (isEditing) {
                    Icon(Icons.Rounded.DeleteForever, "Delete")
                }
            }
        }
    }
}
