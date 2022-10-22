package app.shamilton.common.core.bluetooth

import com.badoo.reaktive.subject.publish.PublishSubject

expect object BluetoothManager {
    val onDeviceFound: PublishSubject<BluetoothDevice>
    val onDeviceConnected: PublishSubject<BluetoothDevice>
    val onDeviceDisconnected: PublishSubject<BluetoothDevice>
    var isConnected: Boolean
        private set

    fun scan()
    fun connect(device: BluetoothDevice): Boolean
    fun disconnect()
    fun write(command: String)
}