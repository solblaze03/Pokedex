package com.example.pokedex.Clases

data class pokeApiEvolution(
    val chain : Chain
)

data class Chain(
    val species: Species,
    val evolves_to: List<Chain>
)

data class Species(
    val name : String,
    val url : String
)