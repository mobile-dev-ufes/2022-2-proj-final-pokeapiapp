package com.mobile.pokeapiapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonListService {

//    @GET("pokemon")
//    suspend fun getPokemonList(): PokemonListModel
//
//    @GET("pokemon/{pokemonId}")
//    suspend fun getSinglePost(@Path("pokemonId")postId: Int): PokemonModel
    @GET("pokemon?limit=20")
    fun getPokemonList(): Call<PokemonListModel>

    @GET("pokemon/{pokemonId}")
    fun getSinglePost(@Path("pokemonId")postId: Int): Call<PokemonModel>

}