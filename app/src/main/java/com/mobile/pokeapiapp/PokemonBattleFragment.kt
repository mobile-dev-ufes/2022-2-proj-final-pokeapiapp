package com.mobile.pokeapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobile.pokeapiapp.databinding.PokemonBattleFragmentBinding

/**
 * Fragmento que irá manipular a tela de batalha do aplicativo
 */
class PokemonBattleFragment : Fragment(R.layout.pokemon_battle_fragment) {
    private var _binding: PokemonBattleFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PokemonBattleFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }
}