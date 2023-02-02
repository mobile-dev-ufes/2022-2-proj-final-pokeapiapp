package com.mobile.pokeapiapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.pokeapiapp.databinding.PokemonListFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonListFragment : Fragment(R.layout.pokemon_list_fragment) {

    private var _binding: PokemonListFragmentBinding? = null
    private val binding get() = _binding!!
    val bpService = ClientRetrofit.createPokemonListService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PokemonListFragmentBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        CoroutineScope(Dispatchers.Main).launch {
            val pokemonList = withContext(Dispatchers.IO) {
                getPokemonList()
            }
            Log.e("COUNT", pokemonList.results.get(0).name)
            binding.recyclerView.adapter = PokemonAdapter(pokemonList.results)
        }

        return binding.root
    }

    private suspend fun getPokemonList(): PokemonListModel {
        return withContext(Dispatchers.IO) {
            val call = bpService.getPokemonList()
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception("Failed to retrieve pokemon list")
            }
        }
    }

    suspend fun getPokeList() = bpService.getPokemonList()

}