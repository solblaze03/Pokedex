package com.example.pokedex.Navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pokedex.Views.Login
import com.example.pokedex.Views.Registro
import com.example.pokedex.Views.detallePokemons
import com.example.pokedex.Views.pantallaPrincipal
import com.example.pokedex.viewModel.VMPokedex
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


@Composable
fun navigationWrapper(auth: FirebaseAuth, viewModel: VMPokedex) {
    val navController = rememberNavController()
    val currentUser: FirebaseUser? = auth.currentUser

    if (currentUser == null) {
        NavHost(
            navController,
            startDestination = Login,
            modifier = Modifier.background(color = Color.DarkGray)
        ) {
            composable<Login>() {
                Login(
                    viewModel,
                    auth,
                    { navController.navigate(Registro) },
                    { navController.navigate(principal) { popUpTo(0){inclusive = true}} })
            }
            composable<Registro>() {
                Registro(
                    viewModel,
                    auth,
                    { navController.navigate(Login) { popUpTo<Login> { inclusive = true } } },
                    { navController.navigate(principal)  {popUpTo(0){inclusive = true}} }
                )
            }
            composable<principal> {
                pantallaPrincipal(auth, viewModel) { e -> navController.navigate(detallePokemon(e))}
            }
            composable<detallePokemon> {
                val pokemon = it.toRoute<detallePokemon>()

                detallePokemons(pokemon.pokemon,{ navController.popBackStack() },
                    viewModel
                )

            }
        }
    } else {
        NavHost(
            navController,
            startDestination = principal,
            modifier = Modifier.background(color = Color.Black)
        ) {
            composable<principal> {
                pantallaPrincipal(auth, viewModel) { navController.navigate(detallePokemon(it)) }
            }
            composable<detallePokemon> {
                val pokemon = it.toRoute<detallePokemon>()

                    detallePokemons(pokemon.pokemon,{ navController.popBackStack() },
                        viewModel
                    )
            }
        }
    }


}