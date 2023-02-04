package com.mobile.pokeapiapp

import com.google.gson.annotations.SerializedName

class PokemonListModel {
    @SerializedName("count")
    var count: Int = 0

    @SerializedName("next")
    var next: String = ""

    @SerializedName("results")
    val results: MutableList<Pokemon?> = mutableListOf()

    data class Pokemon(val name: String, val url: String)

}