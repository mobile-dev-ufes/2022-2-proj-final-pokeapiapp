package com.mobile.pokeapiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mobile.pokeapiapp.databinding.ActivityPokeapiBinding


/**
 * Classe da Activity principal do prrograma, seta o bottomNavigation e chama o fragment de lista de pokemon
 */
class PokeapiActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPokeapiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokeapiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNaviation()

    }

    fun setBottomNaviation(){
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.pokeapi_container_view) as NavHostFragment
        val navController = navHostFrag.navController

        binding.bottomNavMenu.setupWithNavController(navController)
    }

}