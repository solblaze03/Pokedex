package com.example.pokedex.Clases


import retrofit2.http.GET
import retrofit2.http.Path

interface pokeApiServicesDetail {

    @GET("pokemon-form/{id}")
    fun getPokemonsDetail(@Path("id") id : Int): retrofit2.Call<Pokemondetail>
}