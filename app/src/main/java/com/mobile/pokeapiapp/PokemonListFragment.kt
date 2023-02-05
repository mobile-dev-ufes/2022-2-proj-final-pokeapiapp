package com.mobile.pokeapiapp

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.pokeapiapp.databinding.PokemonListFragmentBinding
import kotlinx.coroutines.*
import java.lang.Runnable

class PokemonListFragment : Fragment(R.layout.pokemon_list_fragment) {

    private var _binding: PokemonListFragmentBinding? = null
    private val binding get() = _binding!!
    val pokeApiService = ClientRetrofit.createPokemonListService()
    private var isLoading = false
    lateinit var pokemonList: PokemonListModel
    val context = this
    var filtering = false




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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


        return binding.root
    }
    fun findPokemon(name:String){
        if(name == ""){
            (binding.recyclerView.adapter as PokemonAdapter ).filterList(pokemonList.results)
            filtering= false
            return
        }
        filtering = true
        CoroutineScope(Dispatchers.Main).launch {
            val pokemon = getPokemon(name)

            if(pokemon != null){
                (binding.recyclerView.adapter as PokemonAdapter ).filterList(mutableListOf(
                    PokemonListModel.Pokemon(pokemon.name,pokemon.sprites.frontDefault)
                ))

            }
            else (binding.recyclerView.adapter as PokemonAdapter ).filterList(mutableListOf())
//            binding.recyclerView.adapter!!.notifyDataSetChanged()

        }
    }
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
        }, 1000)
    }


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

    fun showCustomDialog(pokemonId: Int){
        val pokemonBottomSheet = PokemonBottomSheetFragment.newInstance()
        val pokemonIdArg = Bundle()
        pokemonIdArg.putInt("id", pokemonId)
        pokemonBottomSheet.arguments = pokemonIdArg
        pokemonBottomSheet.show(requireActivity().supportFragmentManager, pokemonBottomSheet.tag)
    }
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

}