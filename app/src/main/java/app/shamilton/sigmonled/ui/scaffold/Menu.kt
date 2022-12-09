package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun Menu() {
    val THREE_ELEMENT_LIST = listOf("First", "Second", "Third")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth(),
    ) {
        THREE_ELEMENT_LIST.forEach { text ->
            TextButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = text, fontSize = 30.sp)
            }
        }
    }
}
