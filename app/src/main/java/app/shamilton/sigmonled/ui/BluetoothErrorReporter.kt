package app.shamilton.sigmonled.ui

import android.content.Context
import android.widget.Toast
import app.shamilton.sigmonled.core.devMan
import com.badoo.reaktive.observable.subscribe

class BluetoothErrorReporter(private val context: Context) {
    init {
        devMan.onBluetoothError.subscribe {
            Toast.makeText(context, "Bluetooth Error (${it.errorCode}): ${it.message}", Toast.LENGTH_LONG).show()
        }
    }
}