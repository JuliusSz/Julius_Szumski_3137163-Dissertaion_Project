package com.example.julius_szumski_3137163_dissertaion_project

import com.example.julius_szumski_3137163_dissertaion_project.R
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.julius_szumski_3137163_dissertaion_project.ui.theme.Julius_Szumski_3137163Dissertaion_ProjectTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.model.Document

class CollectionActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Julius_Szumski_3137163Dissertaion_ProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
                            Column (Modifier.fillMaxWidth()){
                                Text("Collection")
                            }
                        })
                    },
                    //floatingActionButton = { FloatingActionButton(onClick = {

                    //})},
                    bottomBar = {
                        BottomAppBar(actions = {
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                                Column() {
                                    IconButton( onClick = {

                                        startActivityIfNeeded(Intent(this@CollectionActivity, PlayerSearchActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("userToken",userID.value ),0)
                                    }) {
                                        Icon(
                                            Icons.Filled.AddCircle,
                                            contentDescription ="Search"
                                        )
                                    }
                                }

                                Column() {
                                    IconButton( onClick = {
                                        startActivityIfNeeded(Intent(this@CollectionActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("userToken",userID.value ),0)
                                    }) {
                                        Icon(
                                            Icons.Filled.Home,
                                            contentDescription ="Overview"
                                        )
                                    }
                                }


                                Column() {
                                    IconButton( onClick = {
                                        startActivityIfNeeded(Intent(this@CollectionActivity, CollectionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("userToken",userID.value ),0)
                                    }, enabled = false) {
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
                    CritterList(this,innerPadding)
                    if(showCard.value){
                        Box(Modifier.fillMaxSize().zIndex(1f), contentAlignment = Alignment.Center) {
                            CritterCard(cardName.value,cardType.value,cardSteps.value,cardDate.value,cardTime.value)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        userID.value = intent.getStringExtra("userToken").toString()
        //Toast.makeText(this,userID.value , Toast.LENGTH_SHORT).show()
        val db = Firebase.firestore
        db.collection("critter").whereEqualTo("User",userID.value).get().addOnSuccessListener { docs->
            critterCollection.clear()
            critterCollection.addAll(docs)
            critterCount.value = critterCollection.size
        }
        //Toast.makeText(this,critterCollection.size.toString(), Toast.LENGTH_SHORT).show()
    }
    private  val userID = mutableStateOf<String>("")
    private var critterCollection= mutableListOf<DocumentSnapshot>()
    private val critterCount = mutableStateOf<Int>(0)
    private val showCard = mutableStateOf<Boolean>(false)
    private val cardName = mutableStateOf<String>("")
    private val cardSteps = mutableStateOf<String>("")
    private val cardType = mutableStateOf<String>("")
    private val cardDate = mutableStateOf<String>("")
    private val cardTime = mutableStateOf<String>("")

    @Composable
    fun CritterList(context: Context, padding: PaddingValues){
        LazyColumn(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ){

            if (critterCount.value == 0){
                item {
                    Text(
                        text = "You have found no Critters yet"
                    )
                }
            }else{
               val critterChunks = critterCollection.chunked(3)
                for (chunk in critterChunks){
                    item {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (critter in chunk){
                                when(critter.get("Type")){
                                    "CommonPH" ->
                                        Box(modifier = Modifier.weight(1f).padding(5.dp).height(100.dp).width(100.dp).border(3.dp, Color.Blue, RoundedCornerShape(10)), contentAlignment = Alignment.Center){
                                            Card(onClick = {
                                                cardName.value = critter.get("Name").toString()
                                                cardType.value = critter.get("Type").toString()
                                                cardSteps.value =critter.get("Steps").toString()
                                                cardDate.value =critter.get("catchDate").toString()
                                                cardTime.value =critter.get("catchTime").toString()
                                                showCard.value = true
                                            }) {
                                                Image(painter = painterResource(R.drawable.bedbug), contentDescription = "bedbug")
                                            }
                                            Text(critter.get("Name").toString())
                                        }
                                    "RarePH" ->
                                        Box(modifier = Modifier.weight(1f).padding(5.dp).height(100.dp).width(100.dp).border(3.dp, Color.Green, RoundedCornerShape(10)), contentAlignment = Alignment.Center){
                                            Card(onClick = {
                                                cardName.value = critter.get("Name").toString()
                                                cardType.value = critter.get("Type").toString()
                                                cardSteps.value =critter.get("Steps").toString()
                                                cardDate.value =critter.get("catchDate").toString()
                                                cardTime.value =critter.get("catchTime").toString()
                                                showCard.value = true
                                            }) {
                                                Image(painter = painterResource(R.drawable.stickbug), contentDescription = "stickbug")
                                            }
                                            Text(critter.get("Name").toString())
                                        }
                                    "LegendaryPH" ->
                                        Box(modifier = Modifier.weight(1f).padding(5.dp).height(100.dp).width(100.dp).border(3.dp, Color.Yellow, RoundedCornerShape(10)), contentAlignment = Alignment.Center){
                                            Card(onClick = {
                                                cardName.value = critter.get("Name").toString()
                                                cardType.value = critter.get("Type").toString()
                                                cardSteps.value =critter.get("Steps").toString()
                                                cardDate.value =critter.get("catchDate").toString()
                                                cardTime.value =critter.get("catchTime").toString()
                                                showCard.value = true
                                            }) {
                                                Image(painter = painterResource(R.drawable.codebug), contentDescription = "codebug")
                                            }
                                            Text(critter.get("Name").toString())
                                        }
                                }

                            }
                            if (chunk.size< 3){
                                val blankspaces: Int = 3-chunk.size
                                repeat(blankspaces){
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                        }
                    }
                }
            }
        }
    }
    @Composable
    fun CritterCard(name: String,type: String,steps: String,date: String, time: String){
        ElevatedCard(modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.7f).offset(y= (-10).dp), elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)) {
            Row(Modifier.padding(20.dp,10.dp),verticalAlignment = Alignment.CenterVertically) {
                Text(name)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    showCard.value = false
                    cardName.value =""
                    cardType.value =""
                    cardSteps.value =""
                    cardDate.value =""
                    cardTime.value =""
                }) {
                    Icon(Icons.Default.Close,"Close")
                }
            }
            Row(Modifier.fillMaxWidth().fillMaxHeight(0.5f).padding(horizontal = 20.dp)) {
                Box(Modifier.fillMaxWidth().fillMaxHeight().border(3.dp,Color.White)) {
                    when (cardName.value){
                        "Bed-Bug"->{
                            Image(painter = painterResource(R.drawable.bedbug), contentDescription = "bedbug",
                                Modifier.fillMaxSize())
                        }
                        "Stick-Bug"->{
                            Image(painter = painterResource(R.drawable.stickbug), contentDescription = "stickbug",
                                Modifier.fillMaxSize())
                        }
                        "Code-Bug"->{
                            Image(painter = painterResource(R.drawable.codebug), contentDescription = "codebug",
                                Modifier.fillMaxSize())
                        }
                    }
                }
            }
            Row(Modifier.padding(20.dp, 10.dp)) {
                Text(type)
            }
            Box(Modifier.padding(20.dp,5.dp).fillMaxSize()) {
                Column() {
                    Row() {
                        Text("Steps: $steps")
                    }
                    Row() {
                        Text("Catch Date: $date")

                    }
                    Row() {
                        Text("Time: $time")
                    }
                }
            }

        }

    }
}