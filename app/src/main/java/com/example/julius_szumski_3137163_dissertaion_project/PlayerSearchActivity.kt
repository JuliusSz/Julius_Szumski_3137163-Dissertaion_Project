package com.example.julius_szumski_3137163_dissertaion_project

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.ParcelUuid
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.julius_szumski_3137163_dissertaion_project.ui.theme.Julius_Szumski_3137163Dissertaion_ProjectTheme
import java.util.UUID

@ExperimentalMaterial3Api
class PlayerSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val missingPermissions = mutableListOf<String>()
                if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED)
                    missingPermissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
                if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                    missingPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
                if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
                    missingPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    missingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)


                if (missingPermissions.isNotEmpty()) {
                    requestPermissions(missingPermissions.toTypedArray(), 2)
                } else {
                    initBLE()
                }
            } else {
                initBLE()
            }


            Julius_Szumski_3137163Dissertaion_ProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
                            Column (Modifier.fillMaxWidth()){
                                Text("Player Search")
                            }
                        })
                    },
                    floatingActionButton = { FloatingActionButton(onClick = {
                        if(searching.value){
                            searching.value=false
                            searchButtonColor.value = Color.Magenta
                        }else{
                            searching.value=true
                            searchButtonColor.value = Color.Cyan

                            BLEadvert.startAdvertising(BLEadvSettings,ComData,object : AdvertiseCallback(){})
                            BLEscanner.startScan(listOf(scanFilter),BLEscanSettings,object : ScanCallback(){
                                @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
                                override fun onScanResult(callbackType: Int, result: ScanResult) {
                                    BLEscanner.stopScan(this)
                                    result.device.connectGatt(this@PlayerSearchActivity, false, gattCallback
                                    )
                                }
                            })

                        }

                    }, containerColor = searchButtonColor.value){
                        Icon(
                            Icons.Filled.AddCircle,
                            contentDescription ="searchButton",
                            tint = Color.Black
                        )
                    }},
                    floatingActionButtonPosition = FabPosition.Center,
                    bottomBar = {
                        BottomAppBar(actions = {
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                                Column() {
                                    IconButton( onClick = {

                                        startActivityIfNeeded(Intent(this@PlayerSearchActivity, PlayerSearchActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("userToken",userID.value ),0)
                                    }, enabled = false) {
                                        Icon(
                                            Icons.Filled.AddCircle,
                                            contentDescription ="Search"
                                        )
                                    }
                                }

                                Column() {
                                    IconButton( onClick = {
                                        startActivityIfNeeded(Intent(this@PlayerSearchActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("userToken",userID.value ),0)
                                    }) {
                                        Icon(
                                            Icons.Filled.Home,
                                            contentDescription ="Overview"
                                        )
                                    }
                                }

                                Column() {
                                    IconButton( onClick = {
                                        startActivityIfNeeded(Intent(this@PlayerSearchActivity, CollectionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("userToken",userID.value ),0)
                                    }) {
                                        Icon(
                                            Icons.Filled.Menu,
                                            contentDescription = "Collection"
                                        )

                                    }
                                }
                            }
                        },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                ) { innerPadding ->
                    /**
                    if(searching.value){
                        Text(
                            text = "You are currently searching for Players in your surrounding",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }else{
                        Text(
                            text = "You are not searching for Players at the moment",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    **/
                    foundPlayersList(this,innerPadding)

                }
            }
        }
    }
    private val searchButtonColor = mutableStateOf<Color>(Color.Magenta)
    private val searching = mutableStateOf<Boolean>(false)
    private  val userID = mutableStateOf<String>("")
    private var foundPlayer = mutableListOf<Int>()

    override fun onResume() {
        super.onResume()
        userID.value = intent.getStringExtra("userToken").toString()
    }

    @Composable
    fun foundPlayersList(context: Context, padding: PaddingValues){
        LazyColumn(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ){
            if(searching.value){

                if (foundPlayer.size == 0 ){
                    item {
                        Text(
                            text = "No Players nearby"
                        )
                    }
                }else{
                    //Logic when Players are found
                }
            }else{
                item {
                    Text(
                        text = "Search for Players By Pressing the center Button"
                    )
                }
            }

        }
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun initBLE() {
        blManager =  getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        blAdapter= blManager.adapter
        BLEadvert = blAdapter.bluetoothLeAdvertiser
        BLEscanner= blAdapter.bluetoothLeScanner

        gatt = blManager.openGattServer(this, object:BluetoothGattServerCallback(){
            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onCharacteristicWriteRequest(
                device: BluetoothDevice,
                requestId: Int,
                characteristic: BluetoothGattCharacteristic,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray
            ) {
                val msg = String(value)

                if (msg == "HELLO") {
                    characteristic.value = "ACK".toByteArray()
                }

                gatt.sendResponse(
                    device,
                    requestId,
                    BluetoothGatt.GATT_SUCCESS,
                    0,
                    characteristic.value
                )
            }
            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onCharacteristicReadRequest(
                device: BluetoothDevice,
                requestId: Int,
                offset: Int,
                characteristic: BluetoothGattCharacteristic
            ) {
                gatt.sendResponse(
                    device,
                    requestId,
                    BluetoothGatt.GATT_SUCCESS,
                    offset,
                    characteristic.value
                )
            }
        })
        gatt.addService(service)
        gattCallback = object : BluetoothGattCallback(){

            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt!!.discoverServices()
                }
            }

            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int){
                val characteristic = gatt!!.getService(BLEIdentifier).getCharacteristic(HandshakeIdentifier)
                characteristic.value = "HELLO".toByteArray()
                gatt.writeCharacteristic(characteristic)
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                val response = String(characteristic.value)
                if(response == "ACK"){
                    runOnUiThread {
                        Toast.makeText(this@PlayerSearchActivity,"Conection Established",Toast.LENGTH_SHORT).show()

                    }

                }
            }




            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onCharacteristicWrite(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                gatt.readCharacteristic(characteristic)
            }

        }
        Toast.makeText(this@PlayerSearchActivity,"successfully initialized", Toast.LENGTH_SHORT).show()
    }
    //GattServer manages Bluetooth Conection (reciever will act as server)
    //https://www.uuidgenerator.net
    val BLEIdentifier: UUID = UUID.fromString("ea86a980-7185-4e52-a37a-11e915c015c8") // Identifies The BLE "frequency" to other devices running the app
    val HandshakeIdentifier: UUID = UUID.fromString("12a0b2fa-d7c0-4090-9287-a8a1903fb410") // Identifies The exchanged PlayerID as the Data that the other device scans for
    lateinit var blManager: BluetoothManager
    lateinit var blAdapter : BluetoothAdapter

    val handshakeID = BluetoothGattCharacteristic(HandshakeIdentifier,BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_WRITE, BluetoothGattCharacteristic.PERMISSION_READ or BluetoothGattCharacteristic.PERMISSION_WRITE)
    val service = BluetoothGattService(BLEIdentifier, BluetoothGattService.SERVICE_TYPE_PRIMARY).apply {
        addCharacteristic(handshakeID)
    }
    lateinit var gatt: BluetoothGattServer

    //BLE Advertiser
    lateinit var BLEadvert : BluetoothLeAdvertiser
    val BLEadvSettings = AdvertiseSettings.Builder().setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY).setConnectable(true).build()
    val ComData = AdvertiseData.Builder().addServiceUuid(ParcelUuid(BLEIdentifier)).build()

    //BLE Scanner

    lateinit var BLEscanner: BluetoothLeScanner
    val scanFilter = ScanFilter.Builder().setServiceUuid(ParcelUuid(BLEIdentifier)).build()
    val BLEscanSettings = ScanSettings.Builder().build()

    //handshake handling
    lateinit var gattCallback: BluetoothGattCallback


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                initBLE()
            } else {
                Toast.makeText(this, "BLE permissions are required", Toast.LENGTH_LONG).show()
            }
        }
    }

}