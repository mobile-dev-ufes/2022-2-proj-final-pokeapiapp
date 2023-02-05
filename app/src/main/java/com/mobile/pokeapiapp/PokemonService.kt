package com.mobile.pokeapiapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

//    @GET("pokemon")
//    suspend fun getPokemonList(): PokemonListModel
//
//    @GET("pokemon/{pokemonId}")
//    suspend fun getSinglePost(@Path("pokemonId")postId: Int): PokemonModel
    @GET("pokemon?limit=20")
    fun getPokemonList(): Call<PokemonListModel>

    @GET("pokemon?")
    fun getPokemonList( @Query("offset")offset: Int,
                        @Query("limit") limit: Int = 20): Call<PokemonListModel>

    @GET("pokemon/{pokemonId}")
    fun getPokemon(@Path("pokemonId")pokemonId: Int): Call<PokemonModel>

    @GET("pokemon/{pokemonName}")
    fun getPokemon(@Path("pokemonId")pokemonName: String): Call<PokemonModel>

}