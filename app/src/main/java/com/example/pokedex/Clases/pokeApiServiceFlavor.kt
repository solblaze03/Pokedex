package com.example.pokedex.Clases

import retrofit2.http.GET
import retrofit2.http.Path

interface pokeApiServiceFlavor {
    @GET("pokemon-species/{id}")
    fun getChain(@Path("id") id : Int) : retrofit2.Call<pokeFlavorText>
}