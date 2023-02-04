package com.mobile.pokeapiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.ProgressBar



class PokemonAdapter(private val pokemonList: MutableList<Pokemon?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_LOADING = 0

    data class Pokemon(val name: String, val url: String)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == VIEW_TYPE_LOADING) {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.pokemon_loading_item, parent, false)
            return LoadingViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() =  pokemonList.size

    override fun getItemViewType(position: Int): Int {
        return if (pokemonList.get(position) == null)  0 else pokemonList.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder)
            pokemonList[position]?.let { holder.bind(it) }
        else
            showLoadingView(holder as LoadingViewHolder, position)

    }
    private fun showLoadingView(
        loadingViewHolder: LoadingViewHolder,
        position: Int
    ) {

    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)

        init {
            progressBar.visibility = View.VISIBLE
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokemonName = itemView.findViewById<TextView>(R.id.pkmnameID)
        private val pokemonImage = itemView.findViewById<ImageView>(R.id.pkmitemID)
        private val favoriteImage = itemView.findViewById<ImageView>(R.id.pkmfavID)
        private val plusImage = itemView.findViewById<ImageView>(R.id.pkmaddID)
        fun bind(pokemon: Pokemon) {
            val pkmId = extractPokemonNumber(pokemon.url)
            val imgUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pkmId.png"
            val pattern = "(.*)-" .toRegex()
            pokemonName.text = pattern.find(pokemon.name)?.value ?:pokemon.name
            Glide.with(itemView.context).load(imgUrl).into(pokemonImage)
            favoriteImage.setOnClickListener({})
            plusImage.setOnClickListener({})
        }
        fun extractPokemonNumber(url: String): Int? {
            val regex = """/pokemon/(\d+)/""".toRegex()
            return regex.find(url)?.groupValues?.get(1)?.toInt()
        }




    }

}

