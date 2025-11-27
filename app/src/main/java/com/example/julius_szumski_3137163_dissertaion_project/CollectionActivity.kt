package com.example.julius_szumski_3137163_dissertaion_project

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.julius_szumski_3137163_dissertaion_project.ui.theme.Julius_Szumski_3137163Dissertaion_ProjectTheme

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

                                        startActivityIfNeeded(Intent(this@CollectionActivity, PlayerSearchActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),0)
                                    }) {
                                        Icon(
                                            Icons.Filled.AddCircle,
                                            contentDescription ="Search"
                                        )
                                    }
                                }

                                Column() {
                                    IconButton( onClick = {
                                        startActivityIfNeeded(Intent(this@CollectionActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),0)
                                    }) {
                                        Icon(
                                            Icons.Filled.Home,
                                            contentDescription ="Overview"
                                        )
                                    }
                                }


                                Column() {
                                    IconButton( onClick = {
                                        startActivityIfNeeded(Intent(this@CollectionActivity, CollectionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),0)
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
                    critterList(this,innerPadding)
                }
            }
        }
    }


private var critterCollection= mutableListOf<Critter>()

    @Composable
    fun critterList(context: Context, padding: PaddingValues){
        LazyColumn(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ){

            if (critterCollection.isEmpty()){
                item {
                    Text(
                        text = "You have found no Critters yet"
                    )
                }
            }else{
                //Logic when Players are found
            }


        }
    }
}