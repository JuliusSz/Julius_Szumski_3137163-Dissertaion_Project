package com.example.julius_szumski_3137163_dissertaion_project

import android.Manifest
import android.R.attr.checked
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
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
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import com.example.julius_szumski_3137163_dissertaion_project.ui.theme.Julius_Szumski_3137163Dissertaion_ProjectTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import org.checkerframework.checker.units.qual.s
import java.security.SecureRandom
import java.util.UUID
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random
import kotlin.random.nextLong

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
                    BLEPermissions.launch(missingPermissions.toTypedArray())
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
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                        if(searching.value == true){
                            role.value = BLERole.Unclear
                            searching.value=false
                            searchButtonColor.value = Color.Magenta
                            BLEadvert.stopAdvertising(advertiseCallback)
                            BLEscanner.stopScan(scanCallback)

                        }else{
                            searching.value=true
                            searchButtonColor.value = Color.Cyan
                            if(role.value == BLERole.Unclear){
                                BLEadvert.startAdvertising(BLEadvSettings,ComData,advertiseCallback)
                                BLEscanner.startScan(listOf(scanFilter),BLEscanSettings,scanCallback)
                            }else{
                                Toast.makeText(this,"smth went wrong", Toast.LENGTH_SHORT).show()
                            }

                        }

                        }, containerColor = searchButtonColor.value){
                        Icon(Icons.Filled.AddCircle, contentDescription ="searchButton", tint = Color.Black)
                    } },
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

        db.collection("users").document(userID.value).collection("BLEID").get().addOnSuccessListener {
            result->

            if(result.isEmpty){
                val shortID= userID.value.take(16)//shortens the userID to a BLE comfortable size
                val dbIDMap = mapOf("BLEID" to shortID)

                db.collection("users").document(userID.value).update(dbIDMap)
                bleID.value= shortID
                //Toast.makeText(this@PlayerSearchActivity,shortID,Toast.LENGTH_SHORT).show()
            }else{
                bleID.value = result.toString()
                //Toast.makeText(this@PlayerSearchActivity,result.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
    val db = Firebase.firestore
    val bleID = mutableStateOf("")
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
                            text = "Searching..."
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

    @OptIn(ExperimentalEncodingApi::class)
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun initBLE() {
        blManager =  getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        blAdapter= blManager.adapter
        BLEadvert = blAdapter.bluetoothLeAdvertiser
        BLEscanner= blAdapter.bluetoothLeScanner
        scanCallback = object : ScanCallback(){
            @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADVERTISE])
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                if (role.value != BLERole.Unclear) {
                    return
                }
                if (blAdapter.address > result.device.address){
                    role.value = BLERole.Server
                    //runOnUiThread {
                        //Toast.makeText(this@PlayerSearchActivity,"I am Server",Toast.LENGTH_SHORT).show()
                    //}
                }else{
                    role.value = BLERole.Client
                    //runOnUiThread {
                        //Toast.makeText(this@PlayerSearchActivity,"I am Client",Toast.LENGTH_SHORT).show()
                    //}
                }


                BLEscanner.stopScan(this)

                if(role.value == BLERole.Client){
                    BLEadvert.stopAdvertising(advertiseCallback)
                    result.device.connectGatt(this@PlayerSearchActivity, false, gattCallback)
                }

            }
        }

        gatt = blManager.openGattServer(this, object:BluetoothGattServerCallback(){
            @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_SCAN])
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
                //runOnUiThread {
                 //   Toast.makeText(this@PlayerSearchActivity,"msgRecieved $msg",Toast.LENGTH_SHORT).show()
                //}
                db.collection("users").whereEqualTo("BLEID",msg).get().addOnSuccessListener{snapshot ->
                    if (!snapshot.isEmpty) {
                        val validIDChars = arrayOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9')
                        val tradeIdList = CharArray(16)
                        for(i in 0..15){

                            tradeIdList[i] = validIDChars[Random.nextInt(0,(validIDChars.size -1))]
                        }
                        val tradeId = String(tradeIdList)
                        val tradeData = mapOf(
                            "Player1" to userID.value,
                            "Player2" to snapshot.elementAt(0).id,
                            "p1Critter" to "",
                            "p2Critter" to "",
                            "p1Confirm" to false,
                            "p2Confirm" to false,
                            "tradeComplete" to false,
                            "tradeCanceled" to false,
                            "p1Connected" to false,
                            "p2Connected" to false
                            )
                        db.collection("trade").document(tradeId).set(tradeData).addOnSuccessListener {
                            gatt.notifyCharacteristicChanged(device, characteristic, false, tradeId.toByteArray())
                            gatt.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null)
                        }

                        //runOnUiThread {
                        //Toast.makeText(this@PlayerSearchActivity,"ACK sent",Toast.LENGTH_SHORT).show()
                        //}
                        searching.value = false
                        startActivityIfNeeded(Intent(this@PlayerSearchActivity, TradeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("tradeID",tradeId).putExtra("userToken", userID.value),0)
                    }else{
                        Log.e("Message",msg)
                        runOnUiThread {
                        Toast.makeText(this@PlayerSearchActivity,msg,Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
            /*
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
            */
            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onDescriptorWriteRequest(
                device: BluetoothDevice,
                requestId: Int,
                descriptor: BluetoothGattDescriptor,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray
            ) {
                if (descriptor.uuid == NotificationIdentifier &&
                    value.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                ) {
                    gatt.sendResponse(
                        device,requestId,BluetoothGatt.GATT_SUCCESS,offset,null
                    )
                }
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

            @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_SCAN])
            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray
            ) {
                //runOnUiThread {
                //Toast.makeText(this@PlayerSearchActivity,"onCharacterisiticChanged",Toast.LENGTH_SHORT).show()
                // }
                val response = String(value)
                db.collection("trade").document(response).get().addOnSuccessListener{snapshot ->
                    if(snapshot.exists()){
                    //runOnUiThread {
                        //Toast.makeText(this@PlayerSearchActivity,"Conection Established",Toast.LENGTH_SHORT).show()
                    //}
                    searching.value = false
                    startActivityIfNeeded(Intent(this@PlayerSearchActivity, TradeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("tradeID",response).putExtra("userToken", userID.value),0)
                    }else{
                        runOnUiThread {
                            Toast.makeText(this@PlayerSearchActivity,"smth Went Wrong",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                val characteristic = gatt.getService(BLEIdentifier).getCharacteristic(HandshakeIdentifier)

                gatt.setCharacteristicNotification(characteristic, true)

                val cccd = characteristic.getDescriptor(NotificationIdentifier)

                gatt.writeDescriptor(cccd,BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)

            }
            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onDescriptorWrite(
                gatt: BluetoothGatt,
                descriptor: BluetoothGattDescriptor,
                status: Int
            ) {
                if (descriptor.characteristic.uuid == HandshakeIdentifier) {
                    val characteristic = descriptor.characteristic
                    gatt.writeCharacteristic(characteristic, bleID.value.toByteArray(), BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
                    //runOnUiThread {
                      //  Toast.makeText(this@PlayerSearchActivity,"Message Sent",Toast.LENGTH_SHORT).show()
                    //}
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
        //Toast.makeText(this@PlayerSearchActivity,"successfully initialized", Toast.LENGTH_SHORT).show()
    }
    //GattServer manages Bluetooth Conection (reciever will act as server, while sender will act as client)
    private val role = mutableStateOf(BLERole.Unclear)

    //https://www.uuidgenerator.net
    val BLEIdentifier: UUID = UUID.fromString("ea86a980-7185-4e52-a37a-11e915c015c8") // Identifies The BLE "frequency" to other devices running the app
    val HandshakeIdentifier: UUID = UUID.fromString("12a0b2fa-d7c0-4090-9287-a8a1903fb410") // Identifies The Gatt server that is beeing communicated with
    val NotificationIdentifier: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    lateinit var blManager: BluetoothManager
    lateinit var blAdapter : BluetoothAdapter
    lateinit var scanCallback: ScanCallback
    var advertiseCallback = object: AdvertiseCallback(){}

    val handshakeID = BluetoothGattCharacteristic(HandshakeIdentifier, BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_READ or BluetoothGattCharacteristic.PERMISSION_WRITE
    ).apply {
        //cccd stands for Client Characteristic Configuration Descriptor.
        //https://devzone.nordicsemi.com/f/nordic-q-a/561/what-does-cccd-mean
        //defines what packets the Server can send to the client
        val cccd = BluetoothGattDescriptor(NotificationIdentifier, BluetoothGattDescriptor.PERMISSION_READ or BluetoothGattDescriptor.PERMISSION_WRITE
        )
        addDescriptor(cccd)
    }

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


    @SuppressLint("MissingPermission")//this Permission is checked for I dont know why it shows up as an error
    private val BLEPermissions = this.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allGranted = permissions.values.all { it }

        if (allGranted) {
            initBLE()
        } else {
            Toast.makeText(this, "You need to give Permission for this to work",Toast.LENGTH_SHORT).show()
        }
    }
    enum class BLERole{
        Unclear,
        Client,
        Server,
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

}
