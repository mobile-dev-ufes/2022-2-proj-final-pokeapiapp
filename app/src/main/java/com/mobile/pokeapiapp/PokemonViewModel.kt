package com.mobile.pokeapiapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel: ViewModel() {

    private var pokemonLiveData = MutableLiveData<PokemonModel>()
    val pokeapiService = ClientRetrofit.createPokemonListService()

    fun getPokemonLiveData(): LiveData<PokemonModel>{
        return pokemonLiveData
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
}