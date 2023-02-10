package app.shamilton.sigmonled.ui.picker

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.color.Color

@Composable
fun SavedColors(
    modifier: Modifier = Modifier,
    onColorSelect: (Color) -> Unit,
    viewModel: ColorPresetsModel,
    isEditing: Boolean,
) {
    Column(
        modifier = modifier
    ) {
        Text("Saved colors:")
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(top = 6.dp)
        ) {
            if(viewModel.savedColors.isEmpty()) {
                Text(
                    text = "There are currently no saved colors",
                    modifier = Modifier
                        .alpha(0.5f)
                )
            } else {
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
    }
}
