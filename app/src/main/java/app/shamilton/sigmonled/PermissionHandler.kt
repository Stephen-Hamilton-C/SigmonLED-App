package app.shamilton.sigmonled

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

/**
 * Handles requesting permissions and checking that all required permissions are granted
 * @param _activity The activity that will be requesting permissions
 */
class PermissionHandler(private val _activity: ComponentActivity) {
    /**
     * Whether all permissions are granted or not. The permissions are determined by
     * a private val in this PermissionHandler
     */
    val allPermissionsGranted: Boolean
        get() {
            _permissions.forEach { permission ->
                if(ContextCompat.checkSelfPermission(_activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    println("Does not have permission $permission")
                    return false
                }
            }

            return true
        }

    /**
     * The array of required permissions
     */
    private val _permissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
        )
    } else {
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }

    /**
     * Asks the user for permissions. If they are all granted, then the _onGranted() callback is
     * invoked. Otherwise, the activity is closed.
     */
    private val _permissionsRequest = _activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionStatuses ->
        var anyDenied = false
        for((_, isGranted) in permissionStatuses) {
            if(!isGranted) {
                anyDenied = true
                break
            }
        }

        if(anyDenied) {
            _activity.finishAndRemoveTask()
        } else if(allPermissionsGranted) {
            _onGranted()
        }
    }

    /**
     * The callback when all permissions are granted
     */
    private var _onGranted: () -> Unit = {}

    /**
     * Requests permissions from the user
     * @param onGranted Callback when all permissions have been granted
     */
    fun requestPermissions(onGranted: () -> Unit) {
        _onGranted = onGranted

        // Create permissions dialog
        val message = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 or higher only needs Bluetooth permissions
            "Bluetooth permissions are required for SigmonLED to function. " +
                    "If this permission is denied, SigmonLED will close."
        } else {
            // Android 11 or lower needs Bluetooth and Location permissions.
            // Location is only used for scanning.
            "Bluetooth and Location permissions are required for SigmonLED to function. " +
                    "Location data is not used, but it is required to scan for nearby SigmonLED-enabled devices. " +
                    "If any of these permissions are denied, SigmonLED will close."
        }

        AlertDialog.Builder(_activity)
            .setTitle("Permissions Required")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                _permissionsRequest.launch(_permissions)
            }
            .create()
            .show()
    }
}