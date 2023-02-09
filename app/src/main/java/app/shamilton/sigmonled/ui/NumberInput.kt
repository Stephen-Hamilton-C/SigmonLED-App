package app.shamilton.sigmonled.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

// TODO: Need better name for incrementDelta
@Composable
fun NumberInput(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    onValueChanged: (Int) -> Unit,
    incrementDelta: Int,
    range: ClosedRange<Int> = Int.MIN_VALUE..Int.MAX_VALUE,
) {
    var number by rememberSaveable { mutableStateOf(value) }
    fun setNumber(newValue: Int) {
        number = newValue.coerceIn(range)
        onValueChanged(number)
    }

    Row(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(0.2f),
            onClick = { setNumber(number - incrementDelta) },
        ) {
            Icon(Icons.Rounded.Remove, "Decrement")
        }
        TextField(
            modifier = Modifier.fillMaxWidth(0.75f),
            label = { Text(label) },
            value = number.toString(),
            onValueChange = {
                try {
                    setNumber(it.toInt())
                } catch(_: NumberFormatException) { }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Button(
            modifier = Modifier.fillMaxWidth(1f),
            onClick = { setNumber(number + incrementDelta) },
        ) {
            Icon(Icons.Rounded.Add, "Increment")
        }
    }
}
