package com.mobile.pokeapiapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.mobile.pokeapiapp.databinding.LoginFragmentBinding

class LoginFragment : Fragment(R.layout.login_fragment) {

    private var _binding : LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        setUpLogin()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onStart() {
//        super.onStart()
//        if (auth.currentUser != null) {
//
//        }
//    }

    private fun setUpLogin(){
        binding.accessButton.setOnClickListener {
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()

            if(email.isEmpty()){
                Toast.makeText(this.context, R.string.toast_login_email_required, Toast.LENGTH_SHORT).show()
            } else if(password.isEmpty()){
                Toast.makeText(this.context, R.string.toast_login_password_required, Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(activity, PokeapiActivity::class.java))
                            activity?.finish()
                        }
                    }
                    .addOnFailureListener {
                        var errorMessage = "Problema com o login, tente novamente"
                        if (it is FirebaseAuthInvalidUserException) {
                            errorMessage = "Usuário inexistente"
                        } else if (it is FirebaseAuthInvalidCredentialsException) {
                            errorMessage = "Senha incorreta"
                        } else if (it is FirebaseNetworkException) {
                            errorMessage = "Falha de conexão"
                        }
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}