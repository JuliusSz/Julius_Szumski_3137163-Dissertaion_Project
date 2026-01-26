package com.example.julius_szumski_3137163_dissertaion_project

import android.Manifest
import android.R
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.julius_szumski_3137163_dissertaion_project.ui.theme.Julius_Szumski_3137163Dissertaion_ProjectTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current


            //Stack overflow
            val permissionLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        registerSensors()
                    }
                }

            LaunchedEffect(Unit) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACTIVITY_RECOGNITION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                } else {
                    registerSensors()
                }
            }
            //Stack overflow end
            if(critterSearching.value){
                searchButtonColor.value = Color.Red
            }else {
                searchButtonColor.value = Color.Green
            }

            Julius_Szumski_3137163Dissertaion_ProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
                            Column (Modifier.fillMaxWidth()){
                                Text("Overview/Homepage")
                            }
                        })
                    },
                    floatingActionButton = { FloatingActionButton(onClick = {
                        if(critterSearching.value){
                            critterSearching.value=false
                            searchButtonColor.value = Color.Red
                        }else{
                            critterSearching.value=true
                            searchButtonColor.value = Color.Green
                            if(stepsTillNextCritter.value == 0){
                                val randNr = Random.nextInt(0 , 100)
                                when(randNr){
                                    in 0..50 ->{
                                        stepsTillNextCritter.value = Random.nextInt(1000,3000)
                                        CurrentCritter.value = CritterType.CommonPH
                                    }
                                    in 51..80->{
                                        stepsTillNextCritter.value = Random.nextInt(2000,5000)
                                        CurrentCritter.value = CritterType.RarePH
                                    }
                                    in 81..100->{
                                        stepsTillNextCritter.value = Random.nextInt(4000,10000)
                                        CurrentCritter.value = CritterType.LegendaryPH
                                    }
                                }
                            }
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

                                        startActivityIfNeeded(Intent(this@MainActivity, PlayerSearchActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("userToken",userID.value ),0)
                                    }) {
                                        Icon(
                                            Icons.Filled.AddCircle,
                                            contentDescription ="Search"
                                        )
                                    }
                                }

                            Column() {
                                IconButton( onClick = {
                                    startActivityIfNeeded(Intent(this@MainActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("userToken",userID.value ),0)
                                }, enabled = false) {
                                    Icon(
                                        Icons.Filled.Home,
                                        contentDescription ="Overview"
                                    )
                                }
                            }

                            Column() {
                                IconButton( onClick = {
                                    startActivity(Intent(this@MainActivity, CollectionActivity::class.java).putExtra("userToken",userID.value ))
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
                    Column(modifier = Modifier.padding(innerPadding)){
                        Row(){
                            Text(text = "Here You will see Your overview")
                        }
                        if (critterSearching.value) {
                            Row() {
                                Text(text = "Searching for Critters")
                            }
                            Row() {
                                Text(text = "Progress: ${currentSearchStepCount.value}/ ${stepsTillNextCritter.value}")
                                if(demo.value){
                                    Button(onClick = {
                                        currentSearchStepCount.value = stepsTillNextCritter.value-1
                                    }) {
                                        Text("[DEBUG] Complete Critter search")
                                    }
                                }
                            }
                        } else {
                            Row() {
                                Text(text = "You are not Searching for Critters at the moment")
                            }
                        }
                        Row(){
                            Text(text = "Total steps taken:${TotalStepsTaken.value}")
                        }
                        //Row(){
                            //Text(text = "Steps taken today:${StepsTakenToday.value}")
                        //}
                        Row(){
                            Text(text = "Critters collected:${nrCrittersCollected.value}")

                        }
                        Row(){
                            Text(text = "[DEBUG]Next Critter: ${CurrentCritter.value.toString()}")
                        }
                    }
                    registerSensors()
                    if(currentSearchStepCount.value >= stepsTillNextCritter.value&& stepsTillNextCritter.value != 0){
                        catchCritter(CurrentCritter.value)
                        currentSearchStepCount.value = 0
                        stepsTillNextCritter.value = 0

                        CurrentCritter.value = CritterType.None
                        critterSearching.value = false
                    }
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        val tempList: ContentValues = ContentValues().apply {
            put("CURRENTSTEPSSEARCH",currentSearchStepCount.value)
            put("CURRENTSTEPSGOAL",stepsTillNextCritter.value)
            put("TODAYSSTEPS",StepsTakenToday.value)
            put("TOTALSTEPS",TotalStepsTaken.value)
            put("USERTOKEN",userID.value)
            put("CURRENTCRITTER",CurrentCritter.value.toString())

        }
        LocalDBHelperStats(this,"Stats",null,1).writableDatabase.insert("Stats",null,tempList)

    }

    override fun onResume() {
        super.onResume()
        userID.value = intent.getStringExtra("userToken").toString()
        val db = Firebase.firestore
        //Toast.makeText(this@MainActivity,userToken.value,Toast.LENGTH_SHORT).show()
        retrieveStats(LocalDBHelperStats(this,"Stats",null,1).writableDatabase)

        db.collection("users").document(userID.value).get().addOnSuccessListener { document ->
                val critters = document.get("CritterCollection") as? List<String> ?: emptyList()
                collectedCritter.clear()
                collectedCritter.addAll(critters)
                nrCrittersCollected.value = collectedCritter.size
        }
    }

    private val CurrentCritter = mutableStateOf<CritterType>(CritterType.None)
    private  val userID = mutableStateOf<String>("")

    private val nrPlayersMet = mutableStateOf<Int>(0)
    private val nrCrittersCollected = mutableStateOf<Int>(0)
    private val StepsTakenToday = mutableStateOf<Int>(0)
    private val TotalStepsTaken = mutableStateOf<Int>(0)
    private val stepsTillNextCritter = mutableStateOf<Int>(0)
    private val currentSearchStepCount = mutableStateOf<Int>(0)
    private val critterSearching = mutableStateOf<Boolean>(false)
    private val searchButtonColor = mutableStateOf<Color>(Color.Red)
    private val collectedCritter = mutableListOf<String>()
    private val demo = mutableStateOf<Boolean>(true)




    private fun registerSensors(){
        val sm: SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if(sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            sm.registerListener(stepListener, sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),SensorManager.SENSOR_DELAY_UI)
        }
    }
    private var stepListener: SensorEventListener = object : SensorEventListener{
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            //notNeeded
        }

        override fun onSensorChanged(p0: SensorEvent?) {
            StepsTakenToday.value++
            if(critterSearching.value){
                currentSearchStepCount.value++
            }
            TotalStepsTaken.value++
            //Toast.makeText(this@MainActivity,"Step!",Toast.LENGTH_SHORT).show()
        }

    }
    //retrieves the last game state from the sqlite Database
    private fun retrieveStats(dbHelperStats: SQLiteDatabase){
        //dbHelperStats.execSQL("DROP TABLE IF EXISTS Stats")

        val tableName: String = "Stats"
        val columns: Array<String> = arrayOf("ID","CURRENTSTEPSSEARCH","CURRENTSTEPSGOAL","TODAYSSTEPS","TOTALSTEPS","CURRENTCRITTER")
        var cursor: Cursor = dbHelperStats.query(tableName,columns,"USERTOKEN = ?",arrayOf(userID.value),null,null,"ID DESC")
        if (cursor.count>0){
            cursor.moveToFirst()
            currentSearchStepCount.value = cursor.getInt(1)
            stepsTillNextCritter.value = cursor.getInt(2)
            StepsTakenToday.value = cursor.getInt(3)
            TotalStepsTaken.value= cursor.getInt(4)
            CurrentCritter.value = convertStringToCritter(cursor.getString(5))
            //Toast.makeText(this@MainActivity,"stats retrieved!",Toast.LENGTH_SHORT).show()
        }
    }
    private fun catchCritter(critterType: CritterType){
        var name: String
        when(critterType){
            CritterType.CommonPH -> name = "Bed-Bug"
            CritterType.RarePH -> name = "Stick-Bug"
            CritterType.LegendaryPH -> name = "Code-Bug"
            else -> name = "How did we get here?"
        }

        val Id: UUID = UUID.randomUUID()
        val db = Firebase.firestore
        val collectedCritterHsh = hashMapOf(
            "ID" to Id.toString(),
            "User" to userID.value,
            "Type" to critterType.toString(),
            "Name" to name,
            "Steps" to currentSearchStepCount.value,
            "catchDate" to LocalDate.now().toString(),
            "catchTime" to LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        )
        collectedCritter.add(collectedCritterHsh.get("ID").toString())
        db.collection("users").document(userID.value).update("CritterCollection",collectedCritter.toList())
        db.collection("critter").document(Id.toString()).set(collectedCritterHsh)
        nrCrittersCollected.value = collectedCritter.size
    }
    fun convertStringToCritter(cString: String): CritterType{
        when(cString){
            "CommonPH" -> return CritterType.CommonPH
            "RarePH" -> return CritterType.RarePH
            "LegendaryPH" -> return CritterType.LegendaryPH
            else -> return CritterType.None
        }
    }
}

enum class CritterType{
    None, CommonPH, RarePH, LegendaryPH
}