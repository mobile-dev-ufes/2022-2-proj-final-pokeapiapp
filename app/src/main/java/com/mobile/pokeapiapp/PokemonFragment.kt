package com.mobile.pokeapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobile.pokeapiapp.databinding.PokemonFragmentBinding

class PokemonFragment:Fragment(R.layout.pokemon_fragment) {
    private var _binding: PokemonFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: PokemonFragmentArgs by navArgs()
    private lateinit var pokemonVM: PokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonVM = ViewModelProvider(requireActivity()).get(PokemonViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PokemonFragmentBinding.inflate(inflater, container, false)
        setObserver()
        pokemonVM.requestPokemonById(args.id)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObserver() {
        pokemonVM.getPokemon().observe(requireActivity(), Observer {
            if (it != null) {
                binding.pokemonCardHeader.text = it.name

                Glide.with(this).load(it.sprites.frontDefault).into(binding.pokemonCardSprite)

                binding.hpValue.text = it.stats.get(0).baseStat.toString()
                binding.attackValue.text = it.stats.get(1).baseStat.toString()
                binding.defenseValue.text = it.stats.get(2).baseStat.toString()
                binding.specialAttackValue.text = it.stats.get(3).baseStat.toString()
                binding.specialDefenseValue.text = it.stats.get(4).baseStat.toString()
                binding.speedValue.text = it.stats.get(5).baseStat.toString()

                binding.totalValue.text =
                    it.stats.fold(0){ acc: Int, pokemonStat: PokemonStat -> (acc + pokemonStat.baseStat) }
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
        })

    }
}