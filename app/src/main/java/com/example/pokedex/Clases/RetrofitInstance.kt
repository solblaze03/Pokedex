package com.example.pokedex.Clases

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val url = "https://pokeapi.co/api/v2/"

    val api : pokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(pokeApiService::class.java)
    }
}