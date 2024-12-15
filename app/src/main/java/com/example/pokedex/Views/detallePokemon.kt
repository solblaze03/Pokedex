package com.example.pokedex.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokedex.Clases.Pokemoninfo
import com.example.pokedex.R
import com.google.gson.Gson


@SuppressLint("DefaultLocale")
@Composable
fun detallePokemons(pokemon: String, regresarPantalla: () -> Boolean) {


    val pokemonSelecc = Gson().fromJson(pokemon, Pokemoninfo::class.java)
    Scaffold(
        topBar = { topBarDetail(pokemonSelecc) { regresarPantalla() } },
        content = { e -> content(e,pokemonSelecc) }
    )


}

@Composable
fun content(paddingValues: PaddingValues, pokemon: Pokemoninfo) {
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Card(
            modifier = Modifier.fillMaxWidth().height(330.dp).padding(top = 60.dp)
                .offset(y = (-(0).dp)),
            shape = RoundedCornerShape(40.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.naranja))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = pokemon.imagen,
                    contentDescription = "Charizard", modifier = Modifier.size(210.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Text(pokemon.nombre.capitalize(), fontSize = 32.sp, fontWeight = FontWeight.SemiBold)
    }
}


@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarDetail(pokemon: Pokemoninfo, regresarPantalla: () -> Boolean) {

    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.naranja)),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth().background(color = colorResource(R.color.naranja)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { regresarPantalla() },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = ""
                            )
                        })
                    Text("Pokedex",
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "#${String.format("%03d", pokemon.numero.toInt())}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 35.dp),
                    textAlign = TextAlign.End,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )


            }


        }


    )

}
