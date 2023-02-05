package com.mobile.pokeapiapp

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.pokeapiapp.databinding.PokemonListFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonListFragment : Fragment(R.layout.pokemon_list_fragment) {

    private var _binding: PokemonListFragmentBinding? = null
    private val binding get() = _binding!!
    val bpService = ClientRetrofit.createPokemonListService()
    private var isLoading = false
    lateinit var pokemonList: PokemonListModel
    val context = this



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

        // APAGAR vvvvvvv
        binding.botaoTest.setOnClickListener{
            showCustomDialog(1)
        }
        // APAGAR ^^^^^^^

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun loadList() {
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
            val call = bpService.getPokemonList()
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception("Failed to retrieve pokemon list")
            }
        }
    }

    fun showCustomDialog(pokemonId: Int){
        Log.e("PKMID",pokemonId.toString())
        val pokemonBottomSheet = PokemonBottomSheetFragment.newInstance()
        val pokemonIdArg = Bundle()
        pokemonIdArg.putInt("id", pokemonId)
        pokemonBottomSheet.arguments = pokemonIdArg
        pokemonBottomSheet.show(requireActivity().supportFragmentManager, pokemonBottomSheet.tag)
    }
    private suspend fun getPokemonList(offset: Int): PokemonListModel {
        return withContext(Dispatchers.IO) {
            val call = bpService.getPokemonList(offset)
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception("Failed to retrieve pokemon list")
            }
        }
    }

}