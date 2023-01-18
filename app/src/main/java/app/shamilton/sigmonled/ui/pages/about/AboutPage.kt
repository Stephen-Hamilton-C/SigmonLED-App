package app.shamilton.sigmonled.ui.pages.about

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AboutPage(modifier: Modifier) {
    Column(modifier = modifier) {
        // TODO: Show icon
        Text("SigmonLED")
        // TODO: Get version
        Text("Version [VERSION]")
        Text("Written by Stephen-Hamilton-C")
        Text("Source code can be found at https://github.com/Stephen-Hamilton-C/SigmonLED")
        Text("This app and its source code are protected under the GPLv3 license. See the link above for the full license.")
    }
}
