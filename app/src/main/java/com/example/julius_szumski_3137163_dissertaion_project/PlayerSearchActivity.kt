package com.example.julius_szumski_3137163_dissertaion_project

import android.content.Context
import android.content.Intent
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

@ExperimentalMaterial3Api
class PlayerSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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

                                        startActivityIfNeeded(Intent(this@PlayerSearchActivity, PlayerSearchActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),0)
                                    }, enabled = false) {
                                        Icon(
                                            Icons.Filled.AddCircle,
                                            contentDescription ="Search"
                                        )
                                    }
                                }

                                Column() {
                                    IconButton( onClick = {
                                        startActivityIfNeeded(Intent(this@PlayerSearchActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),0)
                                    }) {
                                        Icon(
                                            Icons.Filled.Home,
                                            contentDescription ="Overview"
                                        )
                                    }
                                }

                                Column() {
                                    IconButton( onClick = {
                                        startActivityIfNeeded(Intent(this@PlayerSearchActivity, CollectionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),0)
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

    private var foundPlayer = mutableListOf<Int>()

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
}
