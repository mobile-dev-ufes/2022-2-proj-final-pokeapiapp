package com.mobile.pokeapiapp

import com.google.gson.annotations.SerializedName

class PokemonListModel {
    @SerializedName("count")
    val count: Int = 0

    @SerializedName("next")
    val next: String = ""

    @SerializedName("results")
    val results: MutableList<PokemonAdapter.Pokemon> = mutableListOf()

}