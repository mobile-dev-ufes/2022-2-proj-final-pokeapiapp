package com.mobile.pokeapiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class PokemonAdapter(private val pokemonList: List<Pokemon>) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    data class Pokemon(val name: String, val url: String)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = pokemonList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pokemonList[position])
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokemonName = itemView.findViewById<TextView>(R.id.pkmnameID)
        private val pokemonImage = itemView.findViewById<ImageView>(R.id.pkmitemID)

        fun bind(pokemon: Pokemon) {
            val pkmId = extractPokemonNumber(pokemon.url)
            val imgUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pkmId.png"
            pokemonName.text = pokemon.name
            Glide.with(itemView.context).load(imgUrl).into(pokemonImage)
        }
        fun extractPokemonNumber(url: String): Int? {
            val regex = """/pokemon/(\d+)/""".toRegex()
            return regex.find(url)?.groupValues?.get(1)?.toInt()
        }
    }

}

