package app.shamilton.common.core.bluetooth

import com.badoo.reaktive.subject.publish.PublishSubject

actual object BluetoothManager {
    actual val onDeviceFound: PublishSubject<BluetoothDevice>
        get() = TODO("Not yet implemented")
    actual val onDeviceConnected: PublishSubject<BluetoothDevice>
        get() = TODO("Not yet implemented")
    actual val onDeviceDisconnected: PublishSubject<BluetoothDevice>
        get() = TODO("Not yet implemented")
    actual var isConnected: Boolean = false
        get() = TODO("Not yet implemented")

    actual fun scan() {
        TODO("Not yet implemented")
    }

    actual fun connect(device: BluetoothDevice): Boolean {
        TODO("Not yet implemented")
    }

    actual fun disconnect() {
        TODO("Not yet implemented")
    }

    actual fun write(command: String) {
        println("Writing command: $command")
    }
}