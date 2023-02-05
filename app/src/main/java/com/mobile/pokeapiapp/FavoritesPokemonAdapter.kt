package com.mobile.pokeapiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesPokemonAdapter(private val favList: MutableList<PokemonModel>,private val context : PokemonFavoritesFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_list_item, parent, false)
        return ViewHolder(view,this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(favList[position],context)
    }

    override fun getItemCount() = favList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        lateinit var adapter: FavoritesPokemonAdapter
        constructor(itemView: View,adapter: FavoritesPokemonAdapter):this(itemView){
            this.adapter = adapter
        }
        private val pokemonName = itemView.findViewById<TextView>(R.id.pkmnameID)
        private val pokemonImage = itemView.findViewById<ImageView>(R.id.pkmitemID)
        private val pokemonFav = itemView.findViewById<ImageView>(R.id.pkmfavID)
        private val pokemonPlus = itemView.findViewById<ImageView>(R.id.pkmaddID)

        private val db = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()

        fun bind(pokemonModel: PokemonModel, context: PokemonFavoritesFragment) {
            itemView.setOnClickListener{context.showCustomDialog(pokemonModel.id)}
            pokemonName.text = pokemonModel.name
            Glide.with(itemView.context).load(pokemonModel.sprites.frontDefault).into(pokemonImage)
            pokemonFav.visibility = View.GONE
            Glide.with(itemView.context).load("https://cdn-icons-png.flaticon.com/512/7437/7437001.png").into(pokemonPlus)
            pokemonPlus.setOnClickListener {
                removeFavorite(pokemonModel.id)
            }

        }

        private fun removeFavorite(pkmId: Int) {
            val pos = adapterPosition
            db.collection("user").document(auth.uid.toString())
                .update("favorites", FieldValue.arrayRemove(pkmId))
            this.adapter.favList.removeAt(pos)
            this.adapter.notifyItemRemoved(pos)
        }

    }
}