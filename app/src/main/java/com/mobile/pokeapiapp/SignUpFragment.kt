package com.mobile.pokeapiapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.mobile.pokeapiapp.databinding.SignupFragmentBinding

class SignUpFragment : Fragment() {

    private var _binding : SignupFragmentBinding? = null
    private val binding get() = _binding!!
//    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = SignupFragmentBinding.inflate(inflater, container, false)
        setUpSignup()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpSignup(){
        binding.registerButton.setOnClickListener {
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()
            val confirmation = binding.passwordConfirmationField.text.toString()
            val name = binding.usernameField.text.toString()

            if(email.isEmpty()){
                Toast.makeText(this.context, R.string.toast_signup_email_required, Toast.LENGTH_SHORT).show()
            } else if(password.isEmpty()){
                Toast.makeText(this.context, R.string.toast_signup_password_required, Toast.LENGTH_SHORT).show()
            } else if(confirmation.isEmpty()){
                Toast.makeText(this.context, R.string.toast_signup_password_check_required, Toast.LENGTH_SHORT).show()
            } else if(name.isEmpty()){
                Toast.makeText(this.context, R.string.toast_signup_username_required, Toast.LENGTH_SHORT).show()
            } else if(password != confirmation){
                Toast.makeText(this.context, R.string.toast_signup_password_check, Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
//                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener({
//                    if(it.isSuccessful) {
//
//                    }
//                }).addOnFailureListener({})
            }
        }
    }
}