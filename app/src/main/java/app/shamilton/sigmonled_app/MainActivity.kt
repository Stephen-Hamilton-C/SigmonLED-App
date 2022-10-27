package app.shamilton.sigmonled_app

import android.Manifest.permission.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import app.shamilton.sigmonled_app.ui.theme.SigmonLEDAppTheme

private const val REQUEST_ENABLE_BT = 1

class MainActivity : ComponentActivity() {
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val blueMan = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        blueMan.adapter
    }

    override fun onResume() {
        super.onResume()
        if(!bluetoothAdapter.isEnabled) {
            ActivityCompat.requestPermissions(this, arrayOf(BLUETOOTH, BLUETOOTH_ADMIN, ACCESS_FINE_LOCATION), REQUEST_ENABLE_BT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SigmonLEDAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SigmonLEDAppTheme {
        Greeting("Android")
    }
}