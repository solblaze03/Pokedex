package com.example.pokedex.Clases

data class pokeApiList(val count: Int, val results: List<Pokemon>)


data class Pokemon(val name: String, val url: String)
