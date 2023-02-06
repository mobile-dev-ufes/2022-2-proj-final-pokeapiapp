package com.mobile.pokeapiapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClientRetrofit {

    companion object {
        private lateinit var INSTANCE: Retrofit
        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        /**
         *
         *
         * @return retona uma instancia para um client que fará as requisições na PokeAPI
         */
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

        /**
         *
         * @return uma instancia para um client com rotas criadas
         */
        fun createPokemonListService(): PokemonService {
            return getClientInstance().create(PokemonService::class.java)
        }

    }




}