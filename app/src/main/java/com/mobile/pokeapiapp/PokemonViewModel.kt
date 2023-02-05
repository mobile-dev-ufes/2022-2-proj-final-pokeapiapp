package com.mobile.pokeapiapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel: ViewModel() {

    private var pokemonLiveData = MutableLiveData<PokemonModel>()
    private var pokemon1LiveData = MutableLiveData<PokemonModel>()
    private var pokemon2LiveData = MutableLiveData<PokemonModel>()
    val pokeapiService = ClientRetrofit.createPokemonListService()

    fun getPokemonLiveData(): LiveData<PokemonModel>{
        return pokemonLiveData
    }

    fun getPokemon1LiveData(): LiveData<PokemonModel>{
        return pokemon1LiveData
    }
    fun getPokemon2LiveData(): LiveData<PokemonModel>{
        return pokemon2LiveData
    }

    fun requestPokemonById(id: Int) {
        val pokemonResponse: Call<PokemonModel> = pokeapiService.getPokemon(id)
        pokemonResponse.enqueue(object : Callback<PokemonModel> {
            override fun onResponse(call: Call<PokemonModel>, response: Response<PokemonModel>) {
                pokemonLiveData.value = response.body()
            }

            override fun onFailure(call: Call<PokemonModel>, t: Throwable) {
                throw t
            }
        })
    }

    fun requestBattlePokemonById(id: Int, opponent: Int) {
        val pokemonResponse: Call<PokemonModel> = pokeapiService.getPokemon(id)
        pokemonResponse.enqueue(object : Callback<PokemonModel> {
            override fun onResponse(call: Call<PokemonModel>, response: Response<PokemonModel>) {
                if (opponent == 1) pokemon1LiveData.value = response.body()
                else pokemon2LiveData.value = response.body()
            }

            override fun onFailure(call: Call<PokemonModel>, t: Throwable) {
                throw t
            }
        })
    }
}