package com.mobile.pokeapiapp

import android.os.Bundle
import android.util.Log
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
    ): View {
        _binding = PokemonFavoritesFragmentBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        db.collection("user").document(auth.uid.toString()).get().addOnSuccessListener { document ->
            CoroutineScope(Dispatchers.Main).launch {
                val list = (document.get("favorites") as List<Int>)
                list.forEach {

                    val pokemon = getPokemon(it)
                    favList.add(pokemon)
                    if (it == list.last())
                        binding.recyclerView.adapter?.notifyItemInserted(list.size)
                }

            }

        }
        binding.recyclerView.adapter = FavoritesPokemonAdapter(favList, this)
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

    fun showCustomDialog(pokemonId: Int) {
        Log.e("PKMID", pokemonId.toString())
        val pokemonBottomSheet = PokemonBottomSheetFragment.newInstance()
        val pokemonIdArg = Bundle()
        pokemonIdArg.putInt("id", pokemonId)
        pokemonBottomSheet.arguments = pokemonIdArg
        pokemonBottomSheet.show(requireActivity().supportFragmentManager, pokemonBottomSheet.tag)
    }
}