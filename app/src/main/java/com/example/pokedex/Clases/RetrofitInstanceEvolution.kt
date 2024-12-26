package com.example.pokedex.Clases

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceEvolution {

    private const val url = "https://pokeapi.co/api/v2/"
    val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service: pokeApiServiceEvolution = retrofit.create(pokeApiServiceEvolution::class.java)
}