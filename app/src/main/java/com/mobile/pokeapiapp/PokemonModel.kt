package com.mobile.pokeapiapp

import com.google.gson.annotations.SerializedName

class PokemonModel {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("name")
    var name: String = ""
    @SerializedName("sprites")
    var sprites: PokemonSprites = PokemonSprites()
    @SerializedName("stats")
    var stats: MutableList<PokemonStat> = mutableListOf()
    @SerializedName("types")
    var types: MutableList<PokemonType> = mutableListOf()
}

class PokemonSprites {
    @SerializedName("front_default")
    var frontDefault: String = ""
}

class PokemonType {
    @SerializedName("type")
    var type: PokemonItem = PokemonItem()
}

class PokemonStat {
    @SerializedName("base_stat")
    var baseStat: Int = 0
    @SerializedName("stat")
    var stat: PokemonItem = PokemonItem()
}

class PokemonItem {
    @SerializedName("name")
    var name: String = ""
}