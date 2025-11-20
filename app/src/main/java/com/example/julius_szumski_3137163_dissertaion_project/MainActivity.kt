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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                    //floatingActionButton = { FloatingActionButton(onClick = {

                    //})},
                    bottomBar = {
                        BottomAppBar(actions = {
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                            Column() {
                                IconButton( onClick = {
                                    startActivity(Intent(this@MainActivity, PlayerSearchActivity::class.java))
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
                    Text(
                        text = "this will be an overview with caught critters,Total steps taken, steps taken today",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Julius_Szumski_3137163Dissertaion_ProjectTheme {
        Greeting("Android")
    }
}