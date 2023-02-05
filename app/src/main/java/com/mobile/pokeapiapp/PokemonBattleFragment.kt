package com.mobile.pokeapiapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mobile.pokeapiapp.databinding.PokemonBattleFragmentBinding
import java.util.*

class PokemonBattleFragment : Fragment(R.layout.pokemon_battle_fragment) {
    private var _binding: PokemonBattleFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var pokemonVM: PokemonViewModel
    private lateinit var pokemonBattleVM: PokemonBattleViewModel
    private var isFirstTimeObserverPokemon1 = true
    private var isFirstTimeObserverPokemon2 = true
    private var pokemon1Total = 0
    private var pokemon2Total = 0
    private var isBothPokemonSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonVM = ViewModelProvider(requireActivity()).get(PokemonViewModel::class.java)
        pokemonBattleVM = ViewModelProvider(requireActivity()).get(PokemonBattleViewModel::class.java)
        if (pokemonBattleVM.pokemon1Id == 0 || pokemonBattleVM.pokemon2Id == 0){
            Toast.makeText(this.context, "Selecione os dois pokemons para saber qual será o vencedor", Toast.LENGTH_SHORT).show()
        }
        isBothPokemonSet = (pokemonBattleVM.pokemon1Id != 0 && pokemonBattleVM.pokemon2Id != 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PokemonBattleFragmentBinding.inflate(inflater, container, false)
        setPokemon1Observer()
        setPokemon2Observer()
        if (pokemonBattleVM.pokemon1Id != 0) pokemonVM.requestPokemonById(pokemonBattleVM.pokemon1Id)
        if (pokemonBattleVM.pokemon2Id != 0) pokemonVM.requestPokemonById(pokemonBattleVM.pokemon2Id)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isBothPokemonSet){
            fight()
        }
    }

    private fun setPokemon1Observer(){
        pokemonVM.getPokemonLiveData().observe(viewLifecycleOwner) {
            if (isFirstTimeObserverPokemon1) {
                isFirstTimeObserverPokemon1 = false
                pokemonVM.getPokemonLiveData().removeObservers(viewLifecycleOwner)
            }

            if (it != null){
                binding.pokemon1Name.text = it.name.replaceFirstChar {
                    it.titlecase(Locale.getDefault())
                }

                Glide.with(this).load(it.sprites.frontDefault).into(binding.pokemon1Sprite)
            }

            pokemon1Total = it.stats.fold(0) { acc: Int, pokemonStat: PokemonStat -> (acc + pokemonStat.baseStat) }

            binding.pokemon1TotalValue.text = pokemon1Total.toString()

        }
    }
    private fun setPokemon2Observer(){
        pokemonVM.getPokemonLiveData().observe(viewLifecycleOwner) {
            if (isFirstTimeObserverPokemon2) {
                isFirstTimeObserverPokemon2 = false
                pokemonVM.getPokemonLiveData().removeObservers(viewLifecycleOwner)
            }

            if (it != null){
                binding.pokemon2Name.text = it.name.replaceFirstChar {
                    it.titlecase(Locale.getDefault())
                }

                Glide.with(this).load(it.sprites.frontDefault).into(binding.pokemon2Sprite)
            }

            pokemon2Total = it.stats.fold(0) { acc: Int, pokemonStat: PokemonStat -> (acc + pokemonStat.baseStat) }

            binding.pokemon2TotalValue.text = pokemon2Total.toString()
        }
    }

    private fun fight(){
        if (pokemon1Total > pokemon2Total) {
            binding.pokemon1FightStatus.text = R.string.winner_text.toString()
            binding.pokemon1CardContainer.setBackgroundColor(Color.parseColor("#${R.color.winner_container.toString()}"))
            binding.pokemon2FightStatus.text = R.string.loser_text.toString()
            binding.pokemon2CardContainer.setBackgroundColor(Color.parseColor("#${R.color.loser_container.toString()}"))
        } else if (pokemon1Total < pokemon2Total) {
            binding.pokemon1FightStatus.text = R.string.loser_text.toString()
            binding.pokemon1CardContainer.setBackgroundColor(Color.parseColor("#${R.color.loser_container.toString()}"))
            binding.pokemon2FightStatus.text = R.string.winner_text.toString()
            binding.pokemon2CardContainer.setBackgroundColor(Color.parseColor("#${R.color.winner_container.toString()}"))
        } else {
            Log.d("COLOR", Color.parseColor("#${R.color.draw_container.toString()}").toString())
            binding.pokemon1FightStatus.text = R.string.draw_text.toString()
            binding.pokemon1CardContainer.setBackgroundColor(Color.parseColor("#${R.color.draw_container.toString()}"))
            binding.pokemon2FightStatus.text = R.string.draw_text.toString()
            binding.pokemon2CardContainer.setBackgroundColor(Color.parseColor("#${R.color.draw_container.toString()}"))
        }
    }
}