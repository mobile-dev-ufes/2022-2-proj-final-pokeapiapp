package com.mobile.pokeapiapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.pokeapiapp.R
import com.mobile.pokeapiapp.databinding.UserFragmentBinding

/**
 * Fragmento que irá manipular a tela do usuario do aplicativo
 */
class UserFragment:Fragment(R.layout.user_fragment){
    private var _binding: UserFragmentBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

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
        db.collection("user").document(auth.currentUser?.uid.toString()).get().addOnSuccessListener {
            if (it != null) {
                binding.nameSectionValue.text = it.getString("name")
            }
        }
        binding.emailSectionValue.text = auth.currentUser?.email
        return binding.root
    }
}
