package com.example.julius_szumski_3137163_dissertaion_project

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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
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
        Toast.makeText(this,critterCollection.size.toString(), Toast.LENGTH_SHORT).show()
    }
    private  val userID = mutableStateOf<String>("")
    private var critterCollection= mutableListOf<DocumentSnapshot>()
    private val critterCount = mutableStateOf<Int>(0)

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
                                            Card() {
                                                Text(critter.get("Name").toString())
                                            }
                                        }
                                    "RarePH" ->
                                        Box(modifier = Modifier.weight(1f).padding(5.dp).height(100.dp).width(100.dp).border(3.dp, Color.Green, RoundedCornerShape(10)), contentAlignment = Alignment.Center){
                                            Card() {
                                                Text(critter.get("Name").toString())
                                            }
                                        }
                                    "LegendaryPH" ->
                                        Box(modifier = Modifier.weight(1f).padding(5.dp).height(100.dp).width(100.dp).border(3.dp, Color.Yellow, RoundedCornerShape(10)), contentAlignment = Alignment.Center){
                                            Card() {
                                                Text(critter.get("Name").toString())
                                            }
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
}