package com.example.pokedex.Clases

import com.example.pokedex.Clases.RetrofitInstanceDetail.retrofit
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

object RetrofitChainInstance {
    private val url = "https://pokeapi.co/api/v2/"

    val api : pokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(pokeApiService::class.java)
    }

    val service: pokeApiServiceChain = retrofit.create(pokeApiServiceChain::class.java)
}