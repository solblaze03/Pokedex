package com.example.pokedex.Views

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.pokedex.Clases.Pokemoninfo
import com.example.pokedex.R
import com.example.pokedex.colors.listColors

import com.example.pokedex.viewModel.VMPokedex
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import java.util.Collections


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun pantallaPrincipal(
    auth: FirebaseAuth,
    viewModel: VMPokedex,
    detallePokemones: (String) -> Unit
) {
    viewModel.resetPokeEvolution()
    viewModel.resetPokemonDetail()
    viewModel.resetEvol()
    LaunchedEffect(Unit) {
        viewModel.fetchPokemonList(1000)
    }
    Scaffold(
        topBar = { topBar() },
        content = { paddingValues -> content(viewModel, paddingValues, detallePokemones) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar() {
    Column {
        TopAppBar(
            title = { Text("Pokedex") },

            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.inversePrimary)
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.inverseSurface
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun content(
    viewModel: VMPokedex,
    paddingValues: PaddingValues,
    detallePokemones: (String) -> Unit
) {

    val listaPokemones by viewModel.pokelist.observeAsState(emptyList())

    val n = 0

    var busqueda by rememberSaveable { mutableStateOf("") }


    if (listaPokemones.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(78.dp)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = MaterialTheme.colorScheme.onSecondary)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 20.dp, end = 10.dp),
                singleLine = true,
                shape = RoundedCornerShape(35.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                placeholder = { Text("Introduce el nombre del pokemon") }
            )

            val filtrarPokemon = remember(busqueda) {
                listaPokemones.filter { e ->
                    e.name.contains(busqueda)
                }
            }
            Spacer(modifier = Modifier.padding(6.dp))
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                items(filtrarPokemon) { e ->
                    val pokeselecc by viewModel.Pokeseleccionado.observeAsState(initial = true)
                    val colorCard by remember { mutableStateOf(Color.Gray) }

                    var array = e.url.split("/")
                    val numeroPokemon = array[array.size - 2]
                    val imagen =
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$numeroPokemon.png"


                    val isDarkTheme = isSystemInDarkTheme()
                    val imagenFondoCard = if (isDarkTheme) R.drawable.imagedark else {
                        R.drawable.fondopokemon
                    }

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(10.dp)
                            .clickable {


                                val pokemon = Pokemoninfo(e.name, numeroPokemon, imagen)
                                val jsonPokemon = Gson().toJson(pokemon)
                                detallePokemones(jsonPokemon)


                            },

                        colors = CardDefaults.cardColors()
                    ) {

                        Column(
                            modifier = Modifier
                                .paint(
                                    painter = painterResource(imagenFondoCard),
                                    contentScale = ContentScale.Crop
                                )
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            cargarImagen(imagen, modifier = Modifier.size(140.dp))
                            Text(
                                "${e.name.capitalize()}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.inverseSurface
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun cargarImagen(url: String, modifier: Modifier) {


    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .decoderFactory(SvgDecoder.Factory()).placeholder(R.drawable.pokemon).build(),
        contentDescription = "",
        modifier = modifier
    )

}