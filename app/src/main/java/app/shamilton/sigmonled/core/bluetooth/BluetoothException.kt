package app.shamilton.sigmonled.core.bluetooth

import android.bluetooth.BluetoothDevice

abstract class BluetoothException(override val message: String, val errorCode: Int) : Exception()
class BluetoothConnectionException(val device: BluetoothDevice, errorCode: Int)
    : BluetoothException("Unexpected error when attempting to connect to device ${device.address}", errorCode)
class BluetoothDisconnectException(val device: BluetoothDevice, errorCode: Int)
    : BluetoothException("Unexpected error when attempting to disconnect from device ${device.address}", errorCode)
class BluetoothWriteException(errorCode: Int)
    : BluetoothException("Unexpected error when attempting to write to connected device", errorCode)
class BluetoothScanException(errorCode: Int)
    : BluetoothException("Unexpected error when scanning for nearby Bluetooth devices.", errorCode)
