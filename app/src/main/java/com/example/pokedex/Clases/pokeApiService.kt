package com.example.pokedex.Clases

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface pokeApiService {

  @GET("pokemon")
  fun getPokemonList(
      @Query("limit") limit: Int,
      @Query("offset") offset: Int = 0
  ) : Call<pokeApiList>
}