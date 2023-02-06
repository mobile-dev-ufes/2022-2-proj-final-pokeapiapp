package com.mobile.pokeapiapp.utils

import android.content.Context

/**
 *  Classe com utilidades do sistema em geral
 */
class Utils {
    companion object {
        val color = mapOf(
            "normal" to   InfoPokemonTypeColor("#C0BFA4", "#000000"),
            "fire" to     InfoPokemonTypeColor("#FF6E30", "#FFFFFF"),
            "water" to    InfoPokemonTypeColor("#3CADFF", "#000000"),
            "grass" to    InfoPokemonTypeColor("#8AE751", "#000000"),
            "flying" to   InfoPokemonTypeColor("#AB9DFF", "#000000"),
            "fight" to    InfoPokemonTypeColor("#F12B2B", "#FFFFFF"),
            "poison" to   InfoPokemonTypeColor("#9A59DC", "#FFFFFF"),
            "electric" to InfoPokemonTypeColor("#F9D936", "#000000"),
            "ground" to   InfoPokemonTypeColor("#E5D788", "#000000"),
            "rock" to     InfoPokemonTypeColor("#BAAD69", "#000000"),
            "psychic" to  InfoPokemonTypeColor("#F9ADAD", "#000000"),
            "ice" to      InfoPokemonTypeColor("#73EAFB", "#000000"),
            "bug" to      InfoPokemonTypeColor("#A4BE58", "#000000"),
            "ghost" to    InfoPokemonTypeColor("#5552CC", "#FFFFFF"),
            "steel" to    InfoPokemonTypeColor("#BCBCBC", "#000000"),
            "dragon" to   InfoPokemonTypeColor("#7371C2", "#FFFFFF"),
            "dark" to     InfoPokemonTypeColor("#463D3D", "#FFFFFF"),
            "fairy" to    InfoPokemonTypeColor("#E990EB", "#000000")
        )

        fun convertDpToPx(context: Context, dp: Double): Double {
            return dp * context.resources.displayMetrics.density
        }

        fun extractPokemonNumber(url: String): Int {
            val regex = """/pokemon/(\d+).*""".toRegex()
            return regex.find(url)?.groupValues?.get(1)!!.toInt()
        }

        fun getSpriteURL(id: Int): String {
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
        }
    }

    data class InfoPokemonTypeColor(val background: String, val fontColor: String) {}
}
