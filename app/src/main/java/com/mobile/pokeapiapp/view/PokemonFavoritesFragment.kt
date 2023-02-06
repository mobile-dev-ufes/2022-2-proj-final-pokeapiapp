package com.mobile.pokeapiapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.pokeapiapp.ClientRetrofit
import com.mobile.pokeapiapp.FavoritesPokemonAdapter
import com.mobile.pokeapiapp.PokemonModel
import com.mobile.pokeapiapp.R
import com.mobile.pokeapiapp.databinding.PokemonFavoritesFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragmento que irá manipular a tela de favoritos do aplicativo
 */
class PokemonFavoritesFragment : Fragment(R.layout.pokemon_favorites_fragment) {
    private var _binding: PokemonFavoritesFragmentBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val favList = mutableListOf<PokemonModel>()
    val pkmService = ClientRetrofit.createPokemonListService()


    /**
     * Cria uma lista com as informações basicas do pokemon para aparecer a lista de pokemon favoritos
     * na tela
     */
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

    /**
     * Função assincrona que faz as requisições do pokemon a partir de seu id
     *
     * @param [idx] id do pokemon que será buscado na API
     */
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

    /**
     * Chama a classe [PokemonBottomSheetFragment] para mostrar informações do pokemon clicado
     * e seta o id dele em um Bundle
     *
     * @param [pokemonId] id do pokemon que vai aparecer as informações
     */
    fun showCustomDialog(pokemonId: Int) {
        Log.e("PKMID", pokemonId.toString())
        val pokemonBottomSheet = PokemonBottomSheetFragment.newInstance()
        val pokemonIdArg = Bundle()
        pokemonIdArg.putInt("id", pokemonId)
        pokemonBottomSheet.arguments = pokemonIdArg
        pokemonBottomSheet.show(requireActivity().supportFragmentManager, pokemonBottomSheet.tag)
    }
}