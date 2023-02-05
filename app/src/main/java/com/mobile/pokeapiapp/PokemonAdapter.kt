package com.mobile.pokeapiapp

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


class PokemonAdapter(private var pokemonList: MutableList<PokemonListModel.Pokemon?>,private val context: PokemonListFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var itemClickListener: OnItemClickListener

    var favorites  = mutableListOf<Int>()

    private val VIEW_TYPE_LOADING = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        db.collection("user").document(auth.uid.toString())
            .get()
            .addOnSuccessListener { document ->
                val favs = document.get("favorites") as List<Int>

                favs.forEach { it->this.favorites.add(it-1); this.notifyItemChanged(it-1) }
            }

//        Log.e("print",favorites.size.toString())
//        favorites.forEach { it->Log.e("FAV",it.toString()) }
        if(viewType == VIEW_TYPE_LOADING) {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.pokemon_loading_item, parent, false)
            return LoadingViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_list_item, parent, false)
        return ViewHolder(view)
    }
    fun filterList(filterlist: MutableList<PokemonListModel.Pokemon?>) {
        // below line is to add our filtered
        // list in our course array list.
        pokemonList = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    override fun getItemCount() =  pokemonList.size

    override fun getItemViewType(position: Int): Int {
        return if (pokemonList.get(position) == null)  0 else pokemonList.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
//            Log.e("BOOL", favorites.contains(position).toString() + position)

            pokemonList[position]?.let { holder.bind(it, favorites.contains(position),context) }
        }
        else
            showLoadingView(holder as LoadingViewHolder, position)

    }
    private fun showLoadingView(
        loadingViewHolder: LoadingViewHolder,
        position: Int,
    ) {

    }

    fun setOnClickListener(listener: OnItemClickListener){
        this.itemClickListener = listener
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)

        init {
            progressBar.visibility = View.VISIBLE
        }
    }

    class PokemonNotFind(itemView: View) : RecyclerView.ViewHolder(itemView){

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokemonName = itemView.findViewById<TextView>(R.id.pkmnameID)
        private val pokemonImage = itemView.findViewById<ImageView>(R.id.pkmitemID)
        private val favoriteImage = itemView.findViewById<ImageView>(R.id.pkmfavID)
        private val plusImage = itemView.findViewById<ImageView>(R.id.pkmaddID)

        private val db = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()
        private var favorite = false

        fun bind(pokemon: PokemonListModel.Pokemon,favorite : Boolean, context: PokemonListFragment) {

            val pkmId = extractPokemonNumber(pokemon.url)
            this.favorite = favorite
            itemView.setOnClickListener { context .showCustomDialog(pkmId) }
            val imgUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pkmId.png"
            val pattern = "(.*)-".toRegex()
            pokemonName.text = pattern.find(pokemon.name)?.groupValues?.get(1)?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            } ?: pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            Glide.with(itemView.context).load(imgUrl).into(pokemonImage)
            if(!this.favorite)
                Glide.with(itemView.context).load("https://cdn-icons-png.flaticon.com/512/149/149222.png").into(favoriteImage)
            else
                Glide.with(itemView.context).load("https://cdn-icons-png.flaticon.com/512/149/149765.png").into(favoriteImage)
            favoriteImage.setOnClickListener {
                addFatorite(pkmId)
            }
            plusImage.setOnClickListener({})
        }
        private fun addFatorite(pkmId: Int?) {
            if (!favorite){
                Glide.with(itemView.context).load("https://cdn-icons-png.flaticon.com/512/149/149765.png").into(favoriteImage)
                favorite = true
                db.collection("user").document(auth.uid.toString())
                    .update("favorites",FieldValue.arrayUnion(pkmId))
            }
            else {
                Glide.with(itemView.context)
                    .load("https://cdn-icons-png.flaticon.com/512/149/149222.png")
                    .into(favoriteImage)
                db.collection("user").document(auth.uid.toString())
                    .update("favorites",FieldValue.arrayRemove(pkmId))
                favorite = false

            }

        }



        fun extractPokemonNumber(url: String): Int {
            val regex = """/pokemon/(\d+).*""".toRegex()
            return regex.find(url)?.groupValues?.get(1)!!.toInt()
        }
    }



}

