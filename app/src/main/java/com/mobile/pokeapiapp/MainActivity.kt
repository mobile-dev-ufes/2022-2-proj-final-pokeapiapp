package com.mobile.pokeapiapp

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.mobile.pokeapiapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<LoginFragment>(R.id.access_fragment_container_view)
        }

        val loginButton = findViewById<Button>(R.id.login_button)
        val signupButton = findViewById<Button>(R.id.signup_button)

        disableButton(loginButton)

        loginButton.setOnClickListener {
            val fragmentInstance = supportFragmentManager.findFragmentById(R.id.access_fragment_container_view)
            if(fragmentInstance is SignUpFragment){
                enableButton(signupButton)
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<LoginFragment>(R.id.access_fragment_container_view)
            }
            disableButton(loginButton)
        }

        signupButton.setOnClickListener {
            val fragmentInstance = supportFragmentManager.findFragmentById(R.id.access_fragment_container_view)
            if(fragmentInstance is LoginFragment){
                enableButton(loginButton)
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<SignUpFragment>(R.id.access_fragment_container_view)
            }
            disableButton(signupButton)
        }
    }

    private fun disableButton(button: Button){
        button.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_color_lighter, theme))
        button.isEnabled = false
    }
    private fun enableButton(button: Button){
        button.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_color_stronger, theme))
        button.isEnabled = true
    }
    public fun onSignUp() {
        disableButton(binding.loginButton)
        enableButton(binding.signupButton)
    }
}