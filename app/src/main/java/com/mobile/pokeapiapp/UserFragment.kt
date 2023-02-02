package com.mobile.pokeapiapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.mobile.pokeapiapp.databinding.UserFragmentBinding

class UserFragment:Fragment(R.layout.user_fragment){
    private var _binding: UserFragmentBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UserFragmentBinding.inflate(inflater, container, false)
        binding.logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity, MainActivity::class.java))
            requireActivity().finish()
        }
//        binding.nameSectionValue.text = firestore.document().get(auth.currentUser.uid).getName()
        binding.emailSectionValue.text = auth.currentUser?.email
        return binding.root
    }
}
