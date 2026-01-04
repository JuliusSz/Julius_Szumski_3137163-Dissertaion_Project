package com.example.julius_szumski_3137163_dissertaion_project

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class BLE_Manager(private val  context: Context) {
    val blManager = context.getSystemService(BluetoothManager::class.java)
    val adapter = blManager.adapter
    val nearbyDevices = MutableSharedFlow<ScanResult>()

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun search(){
        val scanner = adapter?.bluetoothLeScanner ?: return
        val callback = object : ScanCallback() {
            override fun onScanResult(type: Int, result: ScanResult) {
                CoroutineScope(Dispatchers.IO).launch { nearbyDevices.emit(result) }
            }
        }
        scanner.startScan(callback)
    }

}
