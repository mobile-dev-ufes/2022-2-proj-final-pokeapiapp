package com.mobile.pokeapiapp

import com.google.gson.annotations.SerializedName

class PokemonListModel {
    @SerializedName("count")
    var count: Int = 0

    @SerializedName("next")
    var next: String = ""

    @SerializedName("results")
    val results: MutableList<PokemonAdapter.Pokemon?> = mutableListOf()

}