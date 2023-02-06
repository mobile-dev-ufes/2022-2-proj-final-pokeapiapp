package com.mobile.pokeapiapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


/**
 * Classe que manipula a [RecyclerView] do [PokemonListFragment]
 *
 * @param [pokemonList] a lista de pokemon que vai aparecer na lista
 * @param [context] Contexto para a [PokemonListFragment], é usado para chamar alguns metodos do fragment
 */
class PokemonAdapter(
    private var pokemonList: MutableList<PokemonListModel.Pokemon?>,
    private val context: PokemonListFragment,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var itemClickListener: OnItemClickListener

    var favorites = mutableListOf<Int>()

    private val VIEW_TYPE_LOADING = 0

    /**
     * Pega a lista de favoritos do usuário, caso o pokemon seja favorito aparece uma estrela preenchida no lugar da estrela.
     * Verifica se o proximo item da lista é um pokemon ou loading, que é usado enquanto o codigo carrega mais pokemons a lista
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        db.collection("user").document(auth.uid.toString())
            .get()
            .addOnSuccessListener { document ->
                val favs = document.get("favorites") as List<Int>

                favs.forEach { it -> this.favorites.add(it ); this.notifyItemChanged(it-1) }
            }

//        Log.e("print",favorites.size.toString())
//        favorites.forEach { it->Log.e("FAV",it.toString()) }
        if (viewType == VIEW_TYPE_LOADING) {
            val view: View =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.pokemon_loading_item, parent, false)
            return LoadingViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_list_item, parent, false)
        return ViewHolder(view)
    }


    /**
     * Uma lista para caso o usuário tenha pesquisado por um pokemon
     *
     * @param [filterlist] a nova lista que será mostrada na [RecyclerView]
     */
    fun filterList(filterlist: MutableList<PokemonListModel.Pokemon?>) {

        pokemonList = filterlist
        if (filterlist.size == 1) {
            favorites.clear()
            db.collection("user").document(auth.uid.toString())
                .get()
                .addOnSuccessListener { document ->
                    val favs = document.get("favorites") as List<Int>

                    if (filterlist.get(0)!!.let { favs.contains(it.idx) }) {
                        favorites.add(filterlist.get(0)!!.idx)
                    }

                }
        }

        notifyDataSetChanged()
    }

    override fun getItemCount() = pokemonList.size

    override fun getItemViewType(position: Int): Int {
        return if (pokemonList.get(position) == null) 0 else pokemonList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val idx = holder.extractPokemonNumber(pokemonList[position]!!.url)

            pokemonList[position]?.let { holder.bind(it, favorites.contains(idx), context) }
        } else
            showLoadingView(holder as LoadingViewHolder, position)

    }

    /**
     * Mostra a barra de progresso do loading
     */
    private fun showLoadingView(
        loadingViewHolder: LoadingViewHolder,
        position: Int,
    ) {

    }


    /**
     * Classe da barra de progresso
     */
    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)

        init {
            progressBar.visibility = View.VISIBLE
        }
    }

    /**
     * Classe usada caso o filtro não encontre nenhum pokemon
     */
    class PokemonNotFind(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            TODO("")
        }
    }


    /**
     * Classe que manipula cada pokemon da [RecyclerView]
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokemonName = itemView.findViewById<TextView>(R.id.pkmnameID)
        private val pokemonImage = itemView.findViewById<ImageView>(R.id.pkmitemID)
        private val favoriteImage = itemView.findViewById<ImageView>(R.id.pkmfavID)
        private val plusImage = itemView.findViewById<ImageView>(R.id.pkmaddID)

        private val db = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()
        private var favorite = false

        /**
         * "Escreve" o pokemon da tela e cria listeners nos botões
         *
         * @param [pokemon] O pokemon que irá aparecer naquele item
         * @param [favorite] Um bollean que representa se aquele pokemon está ou não nos favoritos do usuário
         * @param [context] Context do [PokemonListFragment]
         */
        fun bind(
            pokemon: PokemonListModel.Pokemon,
            favorite: Boolean,
            context: PokemonListFragment,
        ) {

            val pkmId = extractPokemonNumber(pokemon.url)
            this.favorite = favorite
            itemView.setOnClickListener { context.showCustomDialog(pkmId) }
            val imgUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pkmId.png"
            val pattern = "(.*)-".toRegex()
            pokemonName.text = pattern.find(pokemon.name)?.groupValues?.get(1)?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
                ?: pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            Glide.with(itemView.context).load(imgUrl).into(pokemonImage)
            if (!this.favorite)
                Glide.with(itemView.context)
                    .load("https://cdn-icons-png.flaticon.com/512/149/149222.png")
                    .into(favoriteImage)
            else
                Glide.with(itemView.context)
                    .load("https://cdn-icons-png.flaticon.com/512/149/149765.png")
                    .into(favoriteImage)
            favoriteImage.setOnClickListener {
                addFavorite(pkmId)
            }
            plusImage.setOnClickListener({})
        }

        /**
         * Adiciona o pokemon aos favoritos caso ainda não seja favorito.
         * Remove o pokemon dos favoritos caso ja esteja na lista de favoritos
         *
         * @param [pkmId] o id do pokemon que será adicionado ou excluido dos favoritos
         */
        private fun addFavorite(pkmId: Int?) {
            if (!favorite) {
                Glide.with(itemView.context)
                    .load("https://cdn-icons-png.flaticon.com/512/149/149765.png")
                    .into(favoriteImage)
                favorite = true
                db.collection("user").document(auth.uid.toString())
                    .update("favorites", FieldValue.arrayUnion(pkmId))
            } else {
                Glide.with(itemView.context)
                    .load("https://cdn-icons-png.flaticon.com/512/149/149222.png")
                    .into(favoriteImage)
                db.collection("user").document(auth.uid.toString())
                    .update("favorites", FieldValue.arrayRemove(pkmId))
                favorite = false

            }

        }

        /**
         * Regex para pegar o id do pokemon a partir de um link, usado para pegar a imagem do pokemon
         *
         * @param [url] O id que contem o id do pokemon
         */
        fun extractPokemonNumber(url: String): Int {
            val regex = """/pokemon/(\d+).*""".toRegex()
            return regex.find(url)?.groupValues?.get(1)!!.toInt()
        }
    }


}

