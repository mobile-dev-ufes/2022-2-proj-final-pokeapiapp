package com.mobile.pokeapiapp.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.mobile.pokeapiapp.*
import com.mobile.pokeapiapp.databinding.PokemonListFragmentBinding
import com.mobile.pokeapiapp.repository.api.client.ClientRetrofit
import com.mobile.pokeapiapp.repository.api.model.PokemonListModel
import com.mobile.pokeapiapp.repository.api.model.PokemonModel
import com.mobile.pokeapiapp.utils.Utils
import com.mobile.pokeapiapp.view.adapter.PokemonAdapter
import com.mobile.pokeapiapp.viewModel.PokemonBattleViewModel
import kotlinx.coroutines.*
import java.lang.Runnable

/**
 * Classe que manipula a framnet de listagem de pokemon
 */
class PokemonListFragment : Fragment(R.layout.pokemon_list_fragment) {

    private var _binding: PokemonListFragmentBinding? = null
    private val binding get() = _binding!!
    val pokeApiService = ClientRetrofit.createPokemonListService()
    private var isLoading = false
    lateinit var pokemonList: PokemonListModel
    val context = this
    var filtering = false
    private lateinit var snackbar: Snackbar

    private lateinit var pokemonBattleVM : PokemonBattleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonBattleVM = ViewModelProvider(requireActivity()).get(PokemonBattleViewModel::class.java)
    }

    /**
     * Seta o manager e o adapter do [RecyclerView] e cria um listener para scroll nessa lista, para
     * caso o elemento mostrado seja o ultimo da lista, adiciona mais elementos a lista.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = PokemonListFragmentBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        CoroutineScope(Dispatchers.Main).launch {
            pokemonList = withContext(Dispatchers.IO) {
                getPokemonList()
            }
            binding.recyclerView.adapter = PokemonAdapter(pokemonList.results,context)
        }
        binding.searchPkm.setOnClickListener { findPokemon(binding.findPkm.text.toString()) }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, idx: Int, dy: Int) {
                super.onScrolled(recyclerView, idx, dy)
                val layoutManager: LinearLayoutManager =
                    binding.recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() >= pokemonList.results.size - 1) {
                        isLoading = true
                        loadList()
                    }
                }
            }
        })

        setUpSnackbar()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (snackbar.isShown) {
            snackbar.dismiss()
        }
    }


    /**
     * Função de buscar pokemon, caso encontre pokemon cria uma lista com eles e mostra na [RecyclerView].
     * Caso a busca esteja vazia mostra todos os pokemons de volta
     *
     * @param [name] o nome do pokemon que está sendo buscado
     */
    fun findPokemon(name:String){
        if(name == ""){
            (binding.recyclerView.adapter as PokemonAdapter).filterList(pokemonList.results)
            filtering= false
            return
        }
        filtering = true
        CoroutineScope(Dispatchers.Main).launch {
            val pokemon = getPokemon(name)

            if(pokemon != null){
                (binding.recyclerView.adapter as PokemonAdapter).filterList(mutableListOf(
                    PokemonListModel.Pokemon(pokemon.name, pokemon.sprites.frontDefault)
                ))

            }
            else (binding.recyclerView.adapter as PokemonAdapter).filterList(mutableListOf())
//            binding.recyclerView.adapter!!.notifyDataSetChanged()

        }
    }

    /**
     * Função que carrega mais pokemon na [RecyclerView]
     */
    private fun loadList() {
        if(filtering) return
        pokemonList.results.add(null)
        binding.recyclerView.adapter!!.notifyItemChanged(pokemonList.results.size)
        val handler = Handler()

        handler.postDelayed(object : Runnable {
            override fun run() {
                CoroutineScope(Dispatchers.Main).launch {
                    val newList = withContext(Dispatchers.IO) {
                        getPokemonList(pokemonList.results.size)
                    }

                    pokemonList.results.removeLast()
                    pokemonList.next = newList.next
                    pokemonList.count = newList.count
                    pokemonList.results.addAll(newList.results)
                    binding.recyclerView.post(object : Runnable {
                        override fun run() {
                            binding.recyclerView.adapter!!.notifyItemInserted(pokemonList.results.size - 1)
                        }
                    })
                    isLoading = false
                }
            }
        }, 500)
    }

    /**
     * Função assincrona que pega uma lista de pokemon por requisição na API
     */
    private suspend fun getPokemonList(): PokemonListModel {
        return withContext(Dispatchers.IO) {
            val call = pokeApiService.getPokemonList()
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception("Failed to retrieve pokemon list")
            }
        }
    }

    /**
     * Busca um pokemon por nome usando a API
     *
     * Caso não encontre com pokemon com o nome, retorna null
     *
     * @param [name] Nome do pokemon que está sendo buscado
     */
    private suspend fun getPokemon(name: String): PokemonModel? {
        return withContext(Dispatchers.IO) {
            val call = pokeApiService.getPokemon(name)
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                null
            }
        }

    }

    /**
     * Chama a classe [PokemonBottomSheetFragment] para mostrar informações do pokemon clicado
     * e seta o id dele em um Bundle
     *
     * @param [pokemonId] id do pokemon que vai aparecer as informações
     */
    fun showCustomDialog(pokemonId: Int){
        val pokemonBottomSheet = PokemonBottomSheetFragment.newInstance()
        val pokemonIdArg = Bundle()
        pokemonIdArg.putInt("id", pokemonId)
        pokemonBottomSheet.arguments = pokemonIdArg
        pokemonBottomSheet.show(requireActivity().supportFragmentManager, pokemonBottomSheet.tag)
    }

    /**
     * Função assincrona que pega uma lista de pokemon por requisição na API a partir de um offset
     *
     * @param [offset] Limite inferior para buscar uma nova lista de pokemon na API
     * @sample getPokemonList(10) retorna uma lista de pokemon apartir do pokemon 10
     */
    private suspend fun getPokemonList(offset: Int): PokemonListModel {
        return withContext(Dispatchers.IO) {
            val call = pokeApiService.getPokemonList(offset)
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception("Failed to retrieve pokemon list")
            }
        }
    }

    fun setUpSnackbar(){
        val activityView = requireActivity().findViewById<View>(R.id.pokeapi_container_view) as ViewGroup
        snackbar = Snackbar.make(activityView, "", Snackbar.LENGTH_INDEFINITE)
        val customSnackView: View = layoutInflater.inflate(R.layout.custom_snackbar, null)
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        val snackLayout = snackbar.view as SnackbarLayout
        val btnErasePokemon1: Button = customSnackView.findViewById(R.id.pokemon_1_remove_button)
        val btnErasePokemon2: Button = customSnackView.findViewById(R.id.pokemon_2_remove_button)
        btnErasePokemon1.setOnClickListener{
            pokemonBattleVM.unsetPokemon(1)
        }
        btnErasePokemon2.setOnClickListener{
            pokemonBattleVM.unsetPokemon(2)
        }
        snackLayout.addView(customSnackView, 0)
        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin + 150)
        snackbar.view.layoutParams = params

        if (pokemonBattleVM.isAnyPokemonSet()) {
            var imageView = customSnackView.findViewById<View>(R.id.pokemon_1_snack_sprite)
            if (!pokemonBattleVM.isPokemon1Set()){
                Glide.with(this).load(R.drawable.empty).into(imageView as ImageView)
            } else {
                Glide.with(this).load(Utils.getSpriteURL(pokemonBattleVM.getPokemon1Id())).into(imageView as ImageView)
            }

            imageView = customSnackView.findViewById<View>(R.id.pokemon_2_snack_sprite)
            if (!pokemonBattleVM.isPokemon2Set()){
                Glide.with(this).load(R.drawable.empty).into(imageView as ImageView)
            } else {
                Glide.with(this).load(Utils.getSpriteURL(pokemonBattleVM.getPokemon2Id())).into(imageView as ImageView)
            }

            snackbar.show()
        }

        pokemonBattleVM.getPokemon1Observable().subscribe{
            val imageView = customSnackView.findViewById<View>(R.id.pokemon_1_snack_sprite)
            if (it == 0){
                Glide.with(this).load(R.drawable.empty).into(imageView as ImageView)
            } else {
                Glide.with(this).load(Utils.getSpriteURL(it)).into(imageView as ImageView)
            }
        }
        pokemonBattleVM.getPokemon2Observable().subscribe{
            val imageView = customSnackView.findViewById<View>(R.id.pokemon_2_snack_sprite)
            if (it == 0){
                Glide.with(this).load(R.drawable.empty).into(imageView as ImageView)
            } else {
                Glide.with(this).load(Utils.getSpriteURL(it)).into(imageView as ImageView)
            }
        }
        pokemonBattleVM.getAnyPokemonObservable().subscribe{
            if (it) {
                snackbar.show()
            } else {
                snackbar.dismiss()
            }
        }
    }

}