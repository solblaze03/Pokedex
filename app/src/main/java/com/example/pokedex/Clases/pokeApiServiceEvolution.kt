package com.example.pokedex.Clases

import retrofit2.http.GET
import retrofit2.http.Path

interface pokeApiServiceEvolution {

    @GET("evolution-chain/{id}")
    fun getEvolutionChain(@Path("id") id : Int) : retrofit2.Call<pokeApiEvolution>

}