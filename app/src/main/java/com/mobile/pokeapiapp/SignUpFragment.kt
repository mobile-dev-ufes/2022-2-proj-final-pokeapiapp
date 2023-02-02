package com.mobile.pokeapiapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.mobile.pokeapiapp.databinding.SignupFragmentBinding

class SignUpFragment : Fragment() {

    private var _binding : SignupFragmentBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = SignupFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSignup()
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
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(activity, R.string.toast_signup_success, Toast.LENGTH_SHORT).show()
                        (activity as MainActivity).onSignUp()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.access_fragment_container_view, LoginFragment())
                        transaction.commit()
//                        val navControl = findNavController()
//                        navControl.navigate(R.id.action_signUpFragment_to_loginFragment)
                    }
                }.addOnFailureListener {
                    var errorMessage = R.string.toast_signup_exception
                    if (it is FirebaseAuthWeakPasswordException) {
                        errorMessage = R.string.toast_signup_weak_pass
                    } else if (it is FirebaseAuthInvalidCredentialsException) {
                        errorMessage = R.string.toast_signup_invalid_cred
                    } else if (it is FirebaseAuthUserCollisionException) {
                        errorMessage = R.string.toast_signup_collision
                    } else if (it is FirebaseNetworkException) {
                        errorMessage = R.string.toast_signup_connection_fail
                    }
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}