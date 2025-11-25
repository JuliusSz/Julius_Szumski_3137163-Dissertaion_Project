package com.example.julius_szumski_3137163_dissertaion_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.julius_szumski_3137163_dissertaion_project.ui.theme.Julius_Szumski_3137163Dissertaion_ProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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

                                        startActivityIfNeeded(Intent(this@MainActivity, PlayerSearchActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),0)
                                    }) {
                                        Icon(
                                            Icons.Filled.AddCircle,
                                            contentDescription ="Search"
                                        )
                                    }
                                }

                            Column() {
                                IconButton( onClick = {
                                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                                }) {
                                    Icon(
                                        Icons.Filled.Home,
                                        contentDescription ="Overview"
                                    )
                                }
                            }


                            Column() {
                                IconButton( onClick = {
                                    startActivity(Intent(this@MainActivity, CollectionActivity::class.java))
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
                            }
                        } else {
                            Row() {
                                Text(text = "You are not Searching for Critters at the moment")
                            }
                        }
                        Row(){
                            Text(text = "Total steps taken:${TotalStepsTaken.value}")
                        }
                        Row(){
                            Text(text = "Steps taken today:${StepsTakenToday.value}")
                        }
                        Row(){
                            Text(text = "Critters collected:${nrCrittersCollected.value}")
                        }
                        Row(){
                            Text(text = "Players met:${nrPlayersMet.value}")
                        }
                    }
                    /**Text(
                        text = "this will be an overview with caught critters,Total steps taken, steps taken today",
                        modifier = Modifier.padding(innerPadding)
                    )**/
                }
            }
        }
    }

    private val nrPlayersMet = mutableStateOf<Int>(0)
    private val nrCrittersCollected = mutableStateOf<Int>(0)
    private val StepsTakenToday = mutableStateOf<Int>(0)
    private val TotalStepsTaken = mutableStateOf<Int>(0)
    private val stepsTillNextCritter = mutableStateOf<Int>(0)
    private val currentSearchStepCount = mutableStateOf<Int>(0)
    private val critterSearching = mutableStateOf<Boolean>(false)
    private val searchButtonColor = mutableStateOf<Color>(Color.Red)
}

