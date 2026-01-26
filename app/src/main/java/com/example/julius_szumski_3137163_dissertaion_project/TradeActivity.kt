package com.example.julius_szumski_3137163_dissertaion_project

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.julius_szumski_3137163_dissertaion_project.ui.theme.Julius_Szumski_3137163Dissertaion_ProjectTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

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
                                    db.collection("trade").document(tradeID.value).update(mapOf("tradeCanceled" to true))
                                }) {
                                Icon(Icons.Default.Close,"Close")
                                }
                            }
                        }

                    })

                }) { innerPadding ->
                    Column(Modifier.padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Row() {
                            Text(player.value.toString())
                        }
                        if(player.value == Player.player1){
                            Row() {
                                Text(p2Connected.value.toString())
                            }
                        }else{
                            Row() {
                                Text(p1Connected.value.toString())
                            }
                        }
                        Row() {
                            Text("Your critter:")
                        }
                        Row() {
                            Box(
                                modifier = Modifier.weight(1f).padding(5.dp).height(100.dp).width(100.dp).border(3.dp, Color.Blue, RoundedCornerShape(10)), contentAlignment = Alignment.Center)
                            {
                                Card(onClick = {
                                    showSelecionCard.value = true
                                }) {
                                    if(SelectedCritter.value != null){
                                        //Text(SelectedCritter.value!!.get("Name").toString())
                                        when (SelectedCritter.value!!.get("Name").toString()){
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
                                    }else{
                                        Text("No Critter Selected yet")
                                    }
                                }

                            }
                        }
                        Row() {
                            Icon(Icons.Filled.KeyboardArrowUp,"up", Modifier.size(20.dp),Color.White)
                            Icon(Icons.Filled.KeyboardArrowDown,"down", Modifier.size(20.dp),Color.White)
                        }
                        Row() {
                            Text("Your friends critter")
                        }
                        Row() {
                            Box(
                                modifier = Modifier.weight(1f).padding(5.dp).height(100.dp)
                                    .width(100.dp).border(3.dp, Color.Blue, RoundedCornerShape(10)),
                                contentAlignment = Alignment.Center
                            )
                            {
                                if(otherSelectedCritter.value != null){
                                    //Text(otherSelectedCritter.value!!.get("Name").toString())
                                    when (otherSelectedCritter.value!!.get("Name").toString()){
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
                                }else{
                                    Text("No Critter Selected yet")
                                }

                            }
                        }
                        Row() {
                            Button(onClick = {
                                if (player.value == Player.player1){
                                    db.collection("trade").document(tradeID.value).update(mapOf("p1Confirm" to true))
                                }else{
                                    db.collection("trade").document(tradeID.value).update(mapOf("p2Confirm" to true))
                                }
                            },enabled= confirmAvailable.value) {
                                Text("Confirm")
                            }
                        }


                    }
                    if (showSelecionCard.value){
                        Box(Modifier.fillMaxSize().zIndex(1f), contentAlignment = Alignment.Center) {
                            SelectionCard()
                        }
                    }
                }
            }
        }
    }

    var SelectedCritter =mutableStateOf<DocumentSnapshot?>(null)
    var otherSelectedCritter =mutableStateOf<DocumentSnapshot?>(null)
    var showSelecionCard = mutableStateOf(false)
    var critterCollection= mutableListOf<DocumentSnapshot>()
    val db = Firebase.firestore
    val player = mutableStateOf(Player.none)
    val tradeID= mutableStateOf("")
    val userID= mutableStateOf("")
    val Player1 = mutableStateOf("")
    val Player2 = mutableStateOf("")
    val p1Critter = mutableStateOf("")
    val p2Critter =  mutableStateOf("")
    val p1Confirm =  mutableStateOf(false)
    val p2Confirm =  mutableStateOf(false)
    val tradeComplete =  mutableStateOf(false)
    val tradeCanceled =  mutableStateOf(false)
    val p1Connected = mutableStateOf(false)
    val p2Connected = mutableStateOf(false)
    val confirmAvailable = mutableStateOf(false)

    override fun onResume() {
        super.onResume()
        resetData()
        tradeID.value = intent.getStringExtra("tradeID").toString()
        userID.value = intent.getStringExtra("userToken").toString()

        db.collection("critter").whereEqualTo("User",userID.value).get().addOnSuccessListener { docs->
            critterCollection.clear()
            critterCollection.addAll(docs)
        }

        db.collection("trade").document(tradeID.value).get().addOnSuccessListener {
            documentSnapshot ->
            Player1.value = documentSnapshot.get("Player1").toString()
            Player2.value = documentSnapshot.get("Player2").toString()
            p1Critter.value = documentSnapshot.get("p1Critter").toString()
            p2Critter.value = documentSnapshot.get("p2Critter").toString()
            p1Confirm.value = documentSnapshot.get("p1Confirm") as Boolean
            p2Confirm.value = documentSnapshot.get("p2Confirm")as Boolean
            tradeComplete.value = documentSnapshot.get("tradeComplete")as Boolean
            tradeCanceled.value = documentSnapshot.get("tradeCanceled")as Boolean
            p1Connected.value = documentSnapshot.get("p1Connected")as Boolean
            p2Connected.value = documentSnapshot.get("p2Connected")as Boolean

            if(userID.value == Player1.value){
                player.value = Player.player1
            }else if(userID.value == Player2.value){
                player.value = Player.player2
            }else{
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
            }
            if(player.value == Player.player1){
                db.collection("trade").document(tradeID.value).update(mapOf("p1Connected" to true))
            }else if(player.value == Player.player2){
                db.collection("trade").document(tradeID.value).update(mapOf("p2Connected" to true))
            }else{
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        db.collection("trade").document(tradeID.value).addSnapshotListener {
            documentSnapshot , e ->
            if (e!=null){
                runOnUiThread {
                    Toast.makeText(this,"Database Failure", Toast.LENGTH_SHORT).show()
                }
                return@addSnapshotListener
            }else{
                Player1.value = documentSnapshot?.get("Player1").toString()
                Player2.value = documentSnapshot?.get("Player2").toString()
                p1Critter.value = documentSnapshot?.get("p1Critter").toString()
                p2Critter.value = documentSnapshot?.get("p2Critter").toString()
                p1Confirm.value = documentSnapshot?.get("p1Confirm") as Boolean
                p2Confirm.value = documentSnapshot.get("p2Confirm")as Boolean
                tradeComplete.value = documentSnapshot.get("tradeComplete")as Boolean
                tradeCanceled.value = documentSnapshot.get("tradeCanceled")as Boolean
                p1Connected.value = documentSnapshot.get("p1Connected")as Boolean
                p2Connected.value = documentSnapshot.get("p2Connected")as Boolean

                if(player.value == Player.player1 && p2Critter.value.isNotBlank()){
                    db.collection("critter").document(p2Critter.value).get().addOnSuccessListener {
                        critter->
                        otherSelectedCritter.value = critter
                    }
                }else if (player.value == Player.player2 && p1Critter.value.isNotBlank()){
                    db.collection("critter").document(p1Critter.value).get().addOnSuccessListener {
                            critter->
                        otherSelectedCritter.value = critter
                    }
                }
                if(p1Critter.value.isNotBlank() && p2Critter.value.isNotBlank() && !confirmAvailable.value){
                    confirmAvailable.value = true
                }
                if(player.value == Player.player1){
                    if(p1Confirm.value && p2Confirm.value&& tradeComplete.value ==false){
                        trade()
                        //Toast.makeText(this,"I traded", Toast.LENGTH_SHORT).show()
                    }
                }
                if(tradeComplete.value){
                    startActivityIfNeeded(Intent(this@TradeActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("userToken",userID.value ),0)
                }
                if (tradeCanceled.value){
                    startActivityIfNeeded(Intent(this@TradeActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("userToken",userID.value ),0)
                }
            }
        }

    }
    private fun resetData(){
        Player1.value = ""
        Player2.value = ""
        p1Critter.value = ""
        p2Critter.value = ""
        p1Confirm.value = false
        p2Confirm.value = false
        tradeComplete.value = false
        tradeCanceled.value = false
        p1Connected.value = false
        p2Connected.value = false
    }
    enum class Player{
        player1,
        player2,
        none
    }
    @Composable
    private fun SelectionCard(){
        ElevatedCard(modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.7f).offset(y= (-10).dp), elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)) {

            LazyColumn(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ){
                item {
                    Row(Modifier.padding(20.dp,10.dp),verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            showSelecionCard.value = false
                        }) {
                            Icon(Icons.Default.Close,"Close")
                        }
                    }
                }
                if (critterCollection.size == 0){
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
                                                    if(player.value == Player.player1){
                                                        p1Critter.value = critter.get("ID") as String
                                                        val updateCritter = mapOf("p1Critter" to p1Critter.value)
                                                        db.collection("trade").document(tradeID.value).update(updateCritter)
                                                        db.collection("critter").document(p1Critter.value).get().addOnSuccessListener {
                                                            critter->
                                                            SelectedCritter.value = critter
                                                        }
                                                    }else{
                                                        p2Critter.value = critter.get("ID") as String
                                                        val updateCritter = mapOf("p2Critter" to p2Critter.value)
                                                        db.collection("trade").document(tradeID.value).update(updateCritter)
                                                        db.collection("critter").document(p2Critter.value).get().addOnSuccessListener {
                                                            critter->
                                                            SelectedCritter.value = critter
                                                        }
                                                    }
                                                    showSelecionCard.value = false
                                                }) {
                                                    Image(painter = painterResource(R.drawable.bedbug), contentDescription = "bedbug",
                                                        Modifier.fillMaxSize())
                                                }
                                            }
                                        "RarePH" ->
                                            Box(modifier = Modifier.weight(1f).padding(5.dp).height(100.dp).width(100.dp).border(3.dp, Color.Green, RoundedCornerShape(10)), contentAlignment = Alignment.Center){
                                                Card(onClick = {
                                                    if(player.value == Player.player1){
                                                        p1Critter.value = critter.get("ID") as String
                                                        val updateCritter = mapOf("p1Critter" to p1Critter.value)
                                                        db.collection("trade").document(tradeID.value).update(updateCritter)
                                                        db.collection("critter").document(p1Critter.value).get().addOnSuccessListener {
                                                            critter->
                                                            SelectedCritter.value = critter
                                                        }
                                                    }else{
                                                        p2Critter.value = critter.get("ID") as String
                                                        val updateCritter = mapOf("p2Critter" to p2Critter.value)
                                                        db.collection("trade").document(tradeID.value).update(updateCritter)
                                                        db.collection("critter").document(p2Critter.value).get().addOnSuccessListener {
                                                            critter->
                                                            SelectedCritter.value = critter
                                                        }
                                                    }
                                                    showSelecionCard.value = false
                                                }) {
                                                    Image(painter = painterResource(R.drawable.stickbug), contentDescription = "stickbug",
                                                        Modifier.fillMaxSize())
                                                }
                                            }
                                        "LegendaryPH" ->
                                            Box(modifier = Modifier.weight(1f).padding(5.dp).height(100.dp).width(100.dp).border(3.dp, Color.Yellow, RoundedCornerShape(10)), contentAlignment = Alignment.Center){
                                                Card(onClick = {
                                                    if(player.value == Player.player1){
                                                        p1Critter.value = critter.get("ID") as String
                                                        val updateCritter = mapOf("p1Critter" to p1Critter.value)
                                                        db.collection("trade").document(tradeID.value).update(updateCritter)
                                                        db.collection("critter").document(p1Critter.value).get().addOnSuccessListener {
                                                            critter->
                                                            SelectedCritter.value = critter
                                                        }
                                                    }else{
                                                        p2Critter.value = critter.get("ID") as String
                                                        val updateCritter = mapOf("p2Critter" to p2Critter.value)
                                                        db.collection("trade").document(tradeID.value).update(updateCritter)
                                                        db.collection("critter").document(p2Critter.value).get().addOnSuccessListener {
                                                            critter->
                                                            SelectedCritter.value = critter
                                                        }
                                                    }
                                                    showSelecionCard.value = false
                                                }) {
                                                    Image(painter = painterResource(R.drawable.codebug), contentDescription = "codebug",
                                                        Modifier.fillMaxSize())
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
    private fun trade(){
        val p1Recv = mapOf("User" to Player1.value)
        val p2Recv = mapOf("User" to Player2.value)


        db.collection("users").document(Player1.value).get().addOnSuccessListener { document ->
            val critters= document.get("CritterCollection") as? List<String> ?: emptyList()
            val temp = mutableListOf<String>()
            temp.addAll(critters)
            temp.remove(p1Critter.value)
            temp.add(p2Critter.value)
            val p1Map = mapOf("CritterCollection" to temp.toList())
            db.collection("users").document(Player1.value).update(p1Map)
        }
        db.collection("critter").document(p1Critter.value).update(p2Recv)





        db.collection("users").document(Player2.value).get().addOnSuccessListener { document ->
            val critters= document.get("CritterCollection") as? List<String> ?: emptyList()
            val temp = mutableListOf<String>()
            temp.addAll(critters)
            temp.remove(p2Critter.value)
            temp.add(p1Critter.value)
            val p2Map = mapOf("CritterCollection" to temp.toList())
            db.collection("users").document(Player2.value).update(p2Map)
        }
        db.collection("critter").document(p2Critter.value).update(p1Recv)
        db.collection("trade").document(tradeID.value).update(mapOf("tradeComplete" to true))
    }

    override fun onPause() {
        super.onPause()
        db.collection("trade").document(tradeID.value).update(mapOf("tradeCanceled" to true))
        finish()
    }
}