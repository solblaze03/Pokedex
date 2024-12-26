package com.example.pokedex.Views

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedex.Clases.Pokemondetail
import com.example.pokedex.Clases.Pokemoninfo
import com.example.pokedex.Clases.pokeApiEvolShain
import com.example.pokedex.Clases.pokeApiEvolution
import com.example.pokedex.R
import com.example.pokedex.colors.listColors
import com.example.pokedex.viewModel.VMPokedex
import com.google.gson.Gson
import java.util.Collections
import kotlin.random.Random


@SuppressLint("DefaultLocale")
@Composable
fun detallePokemons(pokemon: String, regresarPantalla: () -> Boolean, viewModel: VMPokedex) {


    val pokemonSelecc = Gson().fromJson(pokemon, Pokemoninfo::class.java)
    val chain by viewModel.chain.observeAsState()
    var num by remember { mutableStateOf(0) }
    var pokemonEvolucionSeleccionado by remember { mutableStateOf(0) }


    val mediaplayer = remember { MediaPlayer() }
    var reproduciendo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mediaplayer.apply {
            setDataSource("https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/${pokemonSelecc.numero}.ogg")
            setOnPreparedListener{
                start()
                reproduciendo = true
            }
            prepareAsync()
        }
        viewModel.fetchPokemonDetailType(pokemonSelecc.numero.toInt())
        viewModel.fetchPokemonChain(pokemonSelecc.numero.toInt())

    }

    DisposableEffect(Unit) {
        onDispose {
            mediaplayer.release()
        }
    }
    if (!reproduciendo) {
        mediaplayer.start()
    }

    val array = chain?.evolution_chain?.url?.split("/")
    num = array?.get(array?.size?.minus(2) ?: 0)?.toInt() ?: 0
    if (num != 0) {
        pokemonEvolucionSeleccionado = num
    }
    var colorPokemonElemento by remember { mutableStateOf("") }

    val detailPokemon = viewModel.pokemondetail.observeAsState()
    val pokeEvolution = viewModel.evolution.observeAsState()

    detailPokemon.value.let { pokemondetail ->
        colorPokemonElemento = pokemondetail?.types?.get(0)?.type?.name ?: ""
    }





    if (detailPokemon.value != null) {
        Scaffold(
            topBar = { topBarDetail(pokemonSelecc, { regresarPantalla() }, colorPokemonElemento) },
            content = { e ->
                content(
                    e,
                    pokemonSelecc,
                    viewModel,
                    detailPokemon,
                    colorPokemonElemento,
                    pokeEvolution,
                    chain, pokemonEvolucionSeleccionado
                )
            }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(78.dp)
            )
        }
    }


}

@Composable
fun content(
    paddingValues: PaddingValues,
    pokemon: Pokemoninfo,
    viewModel: VMPokedex,
    detailPokemon: State<Pokemondetail?>,
    colorPokemonElemento: String,
    pokeEvolution: State<pokeApiEvolution?>,
    chain: pokeApiEvolShain?,
    numEvolucion: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .padding(top = 60.dp)
                .offset(y = (-(0).dp)),
            shape = RoundedCornerShape(40.dp),
            colors = CardDefaults.cardColors(containerColor = colorElemento(colorPokemonElemento))


        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(pokemon.imagen)
                        .placeholder(R.drawable.pokemon).build(),
                    contentDescription = "Charizard", modifier = Modifier.size(210.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Text(pokemon.nombre.capitalize(), fontSize = 32.sp, fontWeight = FontWeight.SemiBold)


        Spacer(modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier.padding(start = 50.dp, end = 50.dp)) {
            detailPokemon.value.let { pokemondetail ->
                pokemondetail?.types?.forEach { e ->

                    Text(
                        traducirElemento(e.type.name),
                        modifier = Modifier
                            .background(
                                color = colorElemento(e.type.name),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(top = 3.dp, bottom = 3.dp)
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                }
            }
        }

        Spacer(modifier = Modifier.padding(12.dp))
        val array = chain?.evolution_chain?.url?.split("/")
        val num by remember { mutableStateOf(array?.get(array?.size?.minus(2) ?: 0)?.toInt()) }
        LaunchedEffect(Unit) {

        }
        Text("Evoluciones")
        if (pokeEvolution.value != null && num != 0) {
            LazyRow {
                item {
                    pokeEvolution.value.let { pokeApiEvolution ->
                        OutlinedCard(
                            modifier = Modifier
                                .width(130.dp)
                                .height(170.dp)
                                .padding(5.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .paint(
                                        painter = painterResource(R.drawable.imagedark),
                                        contentScale = ContentScale.Crop
                                    ),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                var array = pokeApiEvolution?.chain?.species?.url?.split("/")
                                val numeroPokemon = array?.get(array.size - 2)

                                val imagen =
                                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$numeroPokemon.png"
                                cargarImagen(imagen, Modifier.width(100.dp))
                                Text(pokeApiEvolution?.chain?.species?.name?.capitalize() ?: "")
                            }

                        }
                        pokeApiEvolution?.chain?.evolves_to?.forEach { e ->
                            OutlinedCard(
                                modifier = Modifier
                                    .width(130.dp)
                                    .height(170.dp)
                                    .padding(5.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .paint(
                                            painter = painterResource(R.drawable.imagedark),
                                            contentScale = ContentScale.Crop
                                        ),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    var array = e.species?.url?.split("/")
                                    val numeroPokemon = array?.get(array.size - 2)

                                    val imagen =
                                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$numeroPokemon.png"
                                    cargarImagen(imagen, Modifier.width(100.dp))
                                    Text(e.species?.name?.capitalize() ?: "")
                                }

                            }
                            e.evolves_to.forEach { e ->


                                OutlinedCard(
                                    modifier = Modifier
                                        .width(130.dp)
                                        .height(170.dp)
                                        .padding(5.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .paint(
                                                painter = painterResource(R.drawable.imagedark),
                                                contentScale = ContentScale.Crop
                                            ),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        var array = e?.species?.url?.split("/")
                                        val numeroPokemon = array?.get(array.size - 2)

                                        val imagen =
                                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$numeroPokemon.png"
                                        cargarImagen(imagen, Modifier.width(100.dp))
                                        Text(e.species?.name?.capitalize() ?: "")
                                    }

                                }


                            }
                        }


                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(78.dp)
                        .offset(y = (-50).dp)
                )
            }
        }
        if (numEvolucion != 0) {
            LaunchedEffect(Unit) {
                viewModel.fetchPokemonEvolution(numEvolucion)
            }
        }


    }
}


@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarDetail(
    pokemon: Pokemoninfo,
    regresarPantalla: () -> Boolean,
    colorPokemonElemento: String
) {

    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = colorElemento(
            colorPokemonElemento
        )
    ),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorElemento(colorPokemonElemento)),
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
                    Text(
                        "Pokedex",
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "#${String.format("%03d", pokemon.numero.toInt())}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 35.dp),
                    textAlign = TextAlign.End,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

fun colorElemento(elemento: String): Color {
    val coloresPokemonMap = mapOf(
        "normal" to Color(0xFF696969),    // Gris oscuro
        "fire" to Color(0xFFC71F1F),      // Rojo claro
        "water" to Color(0xFF42A5F5),     // Azul
        "electric" to Color(0xFFB6A500),  // Amarillo brillante
        "grass" to Color(0xFF0DA50F),     // Verde
        "ice" to Color(0xFF4DD0E1),       // Azul claro
        "fighting" to Color(0xFFD32F2F),  // Rojo oscuro
        "poison" to Color(0xFFBA68C8),    // Violeta
        "ground" to Color(0xFFA1887F),    // Marrón
        "flying" to Color(0xFF90A4AE),    // Azul grisáceo
        "psychic" to Color(0xFFF06292),   // Rosa
        "bug" to Color(0xFF8BC34A),       // Verde lima
        "rock" to Color(0xFFBDBDBD),      // Gris
        "ghost" to Color(0xFF9575CD),     // Lavanda
        "dragon" to Color(0xFF7E57C2),    // Violeta oscuro
        "dark" to Color(0xFF757575),      // Gris oscuro
        "steel" to Color(0xFF90A4AE),     // Azul grisáceo
        "fairy" to Color(0xFFCB3970)      // Rosa claro
    )


    return coloresPokemonMap[elemento] ?: Color.Gray

}

fun traducirElemento(elementoIngles: String): String {
    val pokemonTypeTranslations = mapOf(
        "normal" to "Normal",
        "fire" to "Fuego",
        "water" to "Agua",
        "electric" to "Eléctrico",
        "grass" to "Planta",
        "ice" to "Hielo",
        "fighting" to "Lucha",
        "poison" to "Veneno",
        "ground" to "Tierra",
        "flying" to "Volador",
        "psychic" to "Psíquico",
        "bug" to "Bicho",
        "rock" to "Roca",
        "ghost" to "Fantasma",
        "dragon" to "Dragón",
        "dark" to "Siniestro",
        "steel" to "Acero",
        "fairy" to "Hada"
    )
    return pokemonTypeTranslations[elementoIngles] ?: ""
}