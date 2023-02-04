package com.mobile.pokeapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.pokeapiapp.databinding.PokemonFavoritesFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonFavoritesFragment : Fragment(R.layout.pokemon_favorites_fragment) {
    private var _binding: PokemonFavoritesFragmentBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val favList = mutableListOf<PokemonModel>()
    val pkmService = ClientRetrofit.createPokemonListService()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = PokemonFavoritesFragmentBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        db.collection("user").document(auth.uid.toString()).get().addOnSuccessListener { document ->
            (document.get("favorites") as List<Int>).forEach {
                CoroutineScope(Dispatchers.Main).launch {
                    val pokemon = getPokemon(it)
                    favList.add(pokemon)
                    if(it == (document.get("favorites") as List<Int>).last())
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                }

            }

        }
        binding.recyclerView.adapter = FavoritesPokemonAdapter(favList)
        return binding.root
    }

    private suspend fun getPokemon(idx: Int): PokemonModel {
        return withContext(Dispatchers.IO) {
            val call = pkmService.getPokemon(idx)
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception("Failed to retrieve pokemon list")
            }
        }
    }
}