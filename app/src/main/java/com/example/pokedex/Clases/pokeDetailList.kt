package com.example.pokedex.Clases



data class Type(val name: String, val url: String)

data class TypeSlot( val slot: Int, val type: Type )
data class Pokemondetail( val types: List<TypeSlot> )