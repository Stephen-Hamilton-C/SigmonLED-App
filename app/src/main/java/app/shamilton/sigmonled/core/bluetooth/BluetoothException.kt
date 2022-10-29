package app.shamilton.sigmonled.core.bluetooth

import android.bluetooth.BluetoothDevice

abstract class BluetoothException(val errorCode: Int) : Exception()
class BluetoothConnectionException(val device: BluetoothDevice, errorCode: Int) : BluetoothException(errorCode)
class BluetoothWriteException(errorCode: Int) : BluetoothException(errorCode)
class BluetoothScanException(errorCode: Int) : BluetoothException(errorCode)
