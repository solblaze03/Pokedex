package com.example.pokedex.Clases

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface PokemonDetailsService {
    @GET
    fun getPokemonDetails(@Url url: String): Call<Pokemondetail>
}
