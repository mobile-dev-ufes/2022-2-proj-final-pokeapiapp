package com.mobile.pokeapiapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Classe ViewModel para controle dos dados da tela de batalha de pokemon e do card de pokemon
 */
class PokemonViewModel: ViewModel() {

    private var pokemonLiveData = MutableLiveData<PokemonModel>()
    private var pokemon1LiveData = MutableLiveData<PokemonModel>()
    private var pokemon2LiveData = MutableLiveData<PokemonModel>()
    val pokeapiService = ClientRetrofit.createPokemonListService()

    /**
     *  Observavel responsavel pelos pokemon desenhados nos cards com detalhes
     */
    fun getPokemonLiveData(): LiveData<PokemonModel>{
        return pokemonLiveData
    }

    /**
     * Observavel responsavel pelo preenchimento de detalhes do pokemon
     * da posição 1 da tela de batalha
     */
    fun getPokemon1LiveData(): LiveData<PokemonModel>{
        return pokemon1LiveData
    }

    /**
     * Observavel responsavel pelo preenchimento de detalhes do pokemon
     * da posição 2 da tela de batalha
     */
    fun getPokemon2LiveData(): LiveData<PokemonModel>{
        return pokemon2LiveData
    }

    /**
     * Requisição simples para popular o card com detalhes de pokemon
     * @param[id] Id do pokemon a ser buscado
     */
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

    /**
     * Requisição para popular um slot da tela de batalha
     * @param[id] Id do pokemon a ser buscado
     * @param[opponent] Posição na qual ele será colocado
     */
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