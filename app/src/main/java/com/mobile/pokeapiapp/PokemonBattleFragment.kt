package com.mobile.pokeapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mobile.pokeapiapp.databinding.PokemonBattleFragmentBinding
import java.util.*

/**
 * Fragmento que irá manipular a tela de batalha do aplicativo
 */
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
        if (!pokemonBattleVM.isBothPokemonSet()){
            Toast.makeText(this.context, getString(R.string.select_both_pokemon), Toast.LENGTH_SHORT).show()
        }
        isBothPokemonSet = pokemonBattleVM.isBothPokemonSet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PokemonBattleFragmentBinding.inflate(inflater, container, false)
        setPokemon1Observer()
        setPokemon2Observer()
        if (pokemonBattleVM.isPokemon1Set()) pokemonVM.requestBattlePokemonById(pokemonBattleVM.getPokemon1Id(), 1)
        else unsetPokemon1()
        if (pokemonBattleVM.isPokemon2Set()) pokemonVM.requestBattlePokemonById(pokemonBattleVM.getPokemon2Id(), 2)
        else unsetPokemon2()

        binding.pokemon1Card.setOnClickListener{
            showCustomDialog(pokemonBattleVM.getPokemon1Id())
        }
        binding.pokemon2Card.setOnClickListener{
            showCustomDialog(pokemonBattleVM.getPokemon2Id())
        }

        return binding.root
    }

    fun unsetPokemon1(){
        Glide.with(this).load(R.drawable.pokeapi_256).into(binding.pokemon1Sprite)
        binding.pokemon1Name.text = resources.getText(R.string.pokemon_default_header)
        binding.pokemon1TotalValue.text = "0"
    }
    fun unsetPokemon2(){
        Glide.with(this).load(R.drawable.pokeapi_256).into(binding.pokemon2Sprite)
        binding.pokemon2Name.text = resources.getText(R.string.pokemon_default_header)
        binding.pokemon2TotalValue.text = "0"
    }

    private fun setPokemon1Observer(){
        pokemonVM.getPokemon1LiveData().observe(viewLifecycleOwner) {
            if (isFirstTimeObserverPokemon1 && pokemonBattleVM.isPokemon1Set()) {
                isFirstTimeObserverPokemon1 = false
                pokemonVM.getPokemonLiveData().removeObservers(viewLifecycleOwner)

                if (it != null){
                    binding.pokemon1Name.text = it.name.replaceFirstChar {
                        it.titlecase(Locale.getDefault())
                    }

                    Glide.with(this).load(it.sprites.frontDefault).into(binding.pokemon1Sprite)
                }

                pokemon1Total = it.stats.fold(0) { acc: Int, pokemonStat: PokemonStat -> (acc + pokemonStat.baseStat) }

                binding.pokemon1TotalValue.text = pokemon1Total.toString()
                fight()
            }
        }
    }
    private fun setPokemon2Observer(){
        pokemonVM.getPokemon2LiveData().observe(viewLifecycleOwner) {
            if (isFirstTimeObserverPokemon2 && pokemonBattleVM.isPokemon2Set()) {
                isFirstTimeObserverPokemon2 = false
                pokemonVM.getPokemonLiveData().removeObservers(viewLifecycleOwner)
                if (it != null){
                    binding.pokemon2Name.text = it.name.replaceFirstChar {
                        it.titlecase(Locale.getDefault())
                    }

                    Glide.with(this).load(it.sprites.frontDefault).into(binding.pokemon2Sprite)
                }

                pokemon2Total = it.stats.fold(0) { acc: Int, pokemonStat: PokemonStat -> (acc + pokemonStat.baseStat) }

                binding.pokemon2TotalValue.text = pokemon2Total.toString()
                fight()
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

    private fun fight(){
        if (pokemon1Total > pokemon2Total) {
            binding.pokemon1FightStatus.text = getString(R.string.winner_text)
            binding.pokemon1CardContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.winner_container))
            binding.pokemon2FightStatus.text = getString(R.string.loser_text)
            binding.pokemon2CardContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.loser_container))
        } else if (pokemon1Total < pokemon2Total) {
            binding.pokemon1FightStatus.text = getString(R.string.loser_text)
            binding.pokemon1CardContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.loser_container))
            binding.pokemon2FightStatus.text = getString(R.string.winner_text)
            binding.pokemon2CardContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.winner_container))
        } else {
            binding.pokemon1FightStatus.text = getString(R.string.draw_text)
            binding.pokemon1CardContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.draw_container))
            binding.pokemon2FightStatus.text = getString(R.string.draw_text)
            binding.pokemon2CardContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.draw_container))
        }
    }
}