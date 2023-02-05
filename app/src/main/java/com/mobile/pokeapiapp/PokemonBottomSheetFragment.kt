package com.mobile.pokeapiapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobile.pokeapiapp.databinding.PokemonFragmentBinding
import java.util.*

class PokemonBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: PokemonFragmentBinding? = null
    private val binding get() = _binding!!

    //    private val args: PokemonFragmentArgs by navArgs()
    private var pokemonVM: PokemonViewModel? = null
    private var pokemonId: Int = 0
    private var isFirstTimeObserver = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonId = arguments?.getInt("id", 0)!!
        pokemonVM = ViewModelProvider(requireActivity()).get(PokemonViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PokemonFragmentBinding.inflate(inflater, container, false)
        setObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pokemonVM?.requestPokemonById(pokemonId)
    }

    companion object {
        fun newInstance() = PokemonBottomSheetFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pokemonVM?.getPokemonLiveData()?.removeObservers(this)
        pokemonVM = null
        _binding = null
    }

    private fun setObserver() {

        pokemonVM?.getPokemonLiveData()?.observe(this, Observer {
            if(it.id == this.pokemonId) {
                if (isFirstTimeObserver) {
                    isFirstTimeObserver = false
                    pokemonVM?.getPokemonLiveData()!!.removeObservers(this)
                }
                if (it != null) {
                    binding.pokemonCardHeader.text = it.name.replaceFirstChar {
                        it.titlecase(Locale.getDefault())
                    }

                    Glide.with(this).load(it.sprites.frontDefault).into(binding.pokemonCardSprite)

                    it.types.forEach { type ->
                        val textView = TextView(this.context)
                        textView.setText(type.type.name)
                        textView.setPadding(Utils.convertDpToPx(this.requireContext(), 5.0).toInt())
                        textView.setBackgroundColor(Color.parseColor(Utils.color[type.type.name]?.background))
                        textView.setTextColor(Color.parseColor(Utils.color[type.type.name]?.fontColor))
                        binding.pokemonCardTypes.addView(textView)
                    }
                    binding.hpValue.text = it.stats.get(0).baseStat.toString()
                    binding.attackValue.text = it.stats.get(1).baseStat.toString()
                    binding.defenseValue.text = it.stats.get(2).baseStat.toString()
                    binding.specialAttackValue.text = it.stats.get(3).baseStat.toString()
                    binding.specialDefenseValue.text = it.stats.get(4).baseStat.toString()
                    binding.speedValue.text = it.stats.get(5).baseStat.toString()

                    binding.totalValue.text =
                        it.stats.fold(0) { acc: Int, pokemonStat: PokemonStat -> (acc + pokemonStat.baseStat) }
                            .toString()
                } else {
                    binding.pokemonCardHeader.text = R.string.pokemon_default_header.toString()

                    binding.hpValue.text = "0"
                    binding.attackValue.text = "0"
                    binding.defenseValue.text = "0"
                    binding.specialAttackValue.text = "0"
                    binding.specialDefenseValue.text = "0"
                    binding.speedValue.text = "0"

                    binding.totalValue.text = "0"
                }
            }
        })

    }
}