package com.example.pokedex.Clases

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceFlavor {

    private const val url = "https://pokeapi.co/api/v2/"
    val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service: pokeApiServiceFlavor = retrofit.create(pokeApiServiceFlavor::class.java)
}