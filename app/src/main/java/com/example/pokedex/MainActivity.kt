package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pokedex.Navigation.navigationWrapper
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.viewModel.VMPokedex
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private val viewModel: VMPokedex = VMPokedex()

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        setContent {
            PokedexTheme {
                 navigationWrapper(auth, viewModel)
            }
        }
    }
}

