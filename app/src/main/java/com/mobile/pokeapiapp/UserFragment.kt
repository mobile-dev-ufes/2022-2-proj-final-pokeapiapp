package com.mobile.pokeapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobile.pokeapiapp.databinding.UserFragmentBinding

class UserFragment:Fragment(R.layout.user_fragment){
    private var _binding: UserFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UserFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
