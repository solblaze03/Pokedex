package com.example.pokedex.Clases

data class PokemonDetails(
    val sprites: Sprites
)

data class Sprites(
    val other: OtherSprites
)

data class OtherSprites(
    val dream_world: DreamWorld
)

data class DreamWorld(
    val front_default: String?
)
