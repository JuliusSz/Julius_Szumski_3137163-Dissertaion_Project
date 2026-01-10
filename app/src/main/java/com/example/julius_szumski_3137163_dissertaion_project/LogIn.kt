package com.example.julius_szumski_3137163_dissertaion_project

import android.R
import android.content.Intent
import android.media.session.MediaSession
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.julius_szumski_3137163_dissertaion_project.ui.theme.Julius_Szumski_3137163Dissertaion_ProjectTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class LogIn : ComponentActivity() {

    val auth = FirebaseAuth.getInstance()


    private val email = mutableStateOf<String>("")
    private val password = mutableStateOf<String>("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Julius_Szumski_3137163Dissertaion_ProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Row(Modifier.padding(50.dp)) {
                            TextField(
                                value = email.value,
                                onValueChange = { email.value = it },
                                label = { Text("Email") }
                            )
                        }
                        Row(Modifier.padding(50.dp, 0.dp)) {
                            TextField(
                                value = password.value,
                                onValueChange = { password.value = it },
                                label = { Text("Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password
                                )
                            )
                        }
                        Row(Modifier.padding(50.dp, 10.dp)) {
                            Column(Modifier.padding(10.dp, 0.dp)) {
                                Button(onClick = {
                                    if(email.value.isNotBlank() || password.value.isNotBlank()){
                                        auth.signInWithEmailAndPassword(email.value, password.value).addOnSuccessListener { correctPw ->
                                            correctPw.user?.getIdToken(true)
                                                ?.addOnSuccessListener { result ->
                                                    onLoginSuccess(correctPw.user!!.uid)
                                                }
                                        }.addOnFailureListener {
                                            Toast.makeText(this@LogIn, "Wrong Password or email", Toast.LENGTH_SHORT).show()
                                        }
                                    }else{
                                        Toast.makeText(this@LogIn, "Email or password are empty", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                ) {
                                    Text("Log In")
                                }
                            }
                            Column(Modifier.padding(10.dp, 0.dp)) {
                                Button(
                                    onClick =
                                        {
                                            if(email.value.isNotBlank() || password.value.isNotBlank() ){
                                                register()
                                            }else{
                                                Toast.makeText(this@LogIn, "Email or password are empty", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                ) {
                                    Text("Register")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    val db = Firebase.firestore
    private fun onLoginSuccess(token: String) {
        startActivity(Intent(this@LogIn, MainActivity::class.java).putExtra("userToken",token ))
    }

    private fun register() {
        auth.createUserWithEmailAndPassword(email.value, password.value)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    val userData = hashMapOf(
                        "Demo" to false,
                        "ID" to user.uid,
                        "Email" to email.value,
                        "CritterCollection" to listOf<String>()
                    )

                    db.collection("users").document(user.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            onLoginSuccess(user.uid)
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@LogIn,
                    "Registration failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}