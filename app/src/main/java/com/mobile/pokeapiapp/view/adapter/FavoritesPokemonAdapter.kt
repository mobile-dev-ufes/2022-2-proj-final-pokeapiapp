package com.mobile.pokeapiapp.view.adapter

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
import com.mobile.pokeapiapp.R
import com.mobile.pokeapiapp.utils.Utils
import com.mobile.pokeapiapp.repository.api.model.PokemonModel
import com.mobile.pokeapiapp.view.PokemonFavoritesFragment


/**
 * Classe que faz a manipulação da RecyclerView da pággina de favoritos
 * @param[favList] Uma lista de pokemon favoritos que irão aparecer na lista
 * @param[context] Contexto do [PokemonFavoritesFragment]
 */
class FavoritesPokemonAdapter(private val favList: MutableList<PokemonModel>, private val context : PokemonFavoritesFragment) :
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


    /**
     * Classe que ira manipular cada item da [RecyclerView]
     *
     * @param [itemView] view que represenda uma instancia  do pokemon_list_item.xml
     * @param [adapter] contexto do [FavoritesPokemonAdapter] para conseguir remover um item da lista
     */
    class ViewHolder(itemView: View, private val adapter: FavoritesPokemonAdapter): RecyclerView.ViewHolder(itemView){

        private val pokemonName = itemView.findViewById<TextView>(R.id.pkmnameID)
        private val pokemonImage = itemView.findViewById<ImageView>(R.id.pkmitemID)
        private val pokemonFav = itemView.findViewById<ImageView>(R.id.pkmfavID)
        private val pokemonPlus = itemView.findViewById<ImageView>(R.id.pkmaddID)

        private val db = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()

        /**
         * "Escreve" o pokemon da tela e cria listeners nos botões
         *
         * @param [pokemonModel] O pokemon que irá aparecer naquele item
         * @param [context] Context do [PokemonFavoritesFragment]
         */
        fun bind(pokemonModel: PokemonModel, context: PokemonFavoritesFragment) {
            itemView.setOnClickListener{context.showCustomDialog(pokemonModel.id)}
            pokemonName.text = pokemonModel.name
            Glide.with(itemView.context).load(pokemonModel.sprites.frontDefault).into(pokemonImage)
            pokemonFav.visibility = View.GONE
            Glide.with(itemView.context).load("https://cdn-icons-png.flaticon.com/512/7437/7437001.png").into(pokemonPlus)
            pokemonPlus.layoutParams.height = Utils.convertDpToPx(itemView.context, 30.0).toInt()
            pokemonPlus.layoutParams.width = Utils.convertDpToPx(itemView.context, 30.0).toInt()
            pokemonPlus.setOnClickListener {
                removeFavorite(pokemonModel.id)
            }

        }

        /**
         * Ao clicar no botão de excluir, remove o pokemon da lista de favoritos da página e do banco de dados
         *
         * @param [pkmId] id do pokemon que será removido da lista
         */
        private fun removeFavorite(pkmId: Int) {
            val pos = adapterPosition
            db.collection("user").document(auth.uid.toString())
                .update("favorites", FieldValue.arrayRemove(pkmId))
            this.adapter.favList.removeAt(pos)
            this.adapter.notifyItemRemoved(pos)
        }

    }
}