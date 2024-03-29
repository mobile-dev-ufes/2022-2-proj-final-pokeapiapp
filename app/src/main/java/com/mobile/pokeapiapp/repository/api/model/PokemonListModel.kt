package com.mobile.pokeapiapp.repository.api.model

import com.google.gson.annotations.SerializedName

/**
 * Classe model de lista de pokemon na API
 */
class PokemonListModel {
    @SerializedName("count")
    var count: Int = 0

    @SerializedName("next")
    var next: String = ""

    @SerializedName("results")
    val results: MutableList<Pokemon?> = mutableListOf()

    data class Pokemon(var name: String, val url: String){
        var idx : Int = 0
        constructor(idx:Int):this("",""){
            this.idx = idx
        }
    }

}