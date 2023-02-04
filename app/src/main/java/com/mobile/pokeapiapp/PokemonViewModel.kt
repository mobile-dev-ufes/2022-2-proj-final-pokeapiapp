package com.mobile.pokeapiapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel: ViewModel() {

    private var pokemon = MutableLiveData<PokemonModel>()
    val pokeapiService = ClientRetrofit.createPokemonListService()

    fun getPokemon(): LiveData<PokemonModel>{
        return pokemon
    }

    fun requestPokemonById(id: Int) {
        val pokemonResponse: Call<PokemonModel> = pokeapiService.getPokemonById(id)
        pokemonResponse.enqueue(object : Callback<PokemonModel> {
            override fun onResponse(call: Call<PokemonModel>, response: Response<PokemonModel>) {
                pokemon.value = response.body()
            }

            override fun onFailure(call: Call<PokemonModel>, t: Throwable) {
                throw t
            }
        })
    }
}