package com.example.julius_szumski_3137163_dissertaion_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.julius_szumski_3137163_dissertaion_project.ui.theme.Julius_Szumski_3137163Dissertaion_ProjectTheme

class TradeActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Julius_Szumski_3137163Dissertaion_ProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar({
                        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                            Column() {
                                Text("Trading")
                            }
                            Spacer(Modifier.fillMaxWidth(0.1f))
                            Column() {
                                IconButton(onClick = {
                                //close
                                }) {
                                Icon(Icons.Default.Close,"Close")
                                }
                            }
                        }

                    })

                }) { innerPadding ->
                    Column(Modifier.padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Row() {
                            Box(
                                modifier = Modifier.weight(1f).padding(5.dp).height(100.dp).width(100.dp).border(3.dp, Color.Blue, RoundedCornerShape(10)), contentAlignment = Alignment.Center)
                            {
                                Text("your critter")
                            }
                        }
                        Row() {
                            Icon(Icons.Filled.KeyboardArrowUp,"up", Modifier.size(20.dp),Color.White)
                            Icon(Icons.Filled.KeyboardArrowDown,"down", Modifier.size(20.dp),Color.White)
                        }
                        Row() {
                            Box(
                                modifier = Modifier.weight(1f).padding(5.dp).height(100.dp)
                                    .width(100.dp).border(3.dp, Color.Blue, RoundedCornerShape(10)),
                                contentAlignment = Alignment.Center
                            )
                            {
                                Text("other Critter")
                            }
                        }
                        Row() {
                            Button(onClick = {
                            }) {
                                Text("Confirm")
                            }
                        }


                    }
                }
            }
        }
    }
}