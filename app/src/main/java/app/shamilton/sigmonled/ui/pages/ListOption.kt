package app.shamilton.sigmonled.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListOption(
    label: @Composable () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    possibleValues: Collection<String>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    ListItem(
        overlineText = label,
        text = { Text(value) },
        trailing = {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                possibleValues.forEach {
                    DropdownMenuItem(onClick = {
                        onValueChange(it)
                        expanded = false
                    }) {
                        Text(it)
                    }
                }
            }
        },
        modifier = modifier.clickable {
            expanded = true
        }
    )
}
