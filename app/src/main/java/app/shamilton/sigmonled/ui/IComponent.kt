package app.shamilton.sigmonled.ui

import androidx.compose.runtime.Composable
import app.shamilton.sigmonled.core.ArduinoCommander

interface IComponent {
    @Composable
    fun Component(commander: ArduinoCommander)
}