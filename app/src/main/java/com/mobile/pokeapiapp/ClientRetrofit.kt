package com.mobile.pokeapiapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClientRetrofit {

    companion object {
        private lateinit var INSTANCE: Retrofit
        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        private fun getClientInstance(): Retrofit {
            val http = OkHttpClient.Builder()
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(http.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return INSTANCE
        }
        fun createPokemonListService(): PokemonListService {
            return getClientInstance().create(PokemonListService::class.java)
        }

    }



}