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

        loginButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_color_lighter, theme))

        loginButton.setOnClickListener {
            val fragmentInstance = supportFragmentManager.findFragmentById(R.id.access_fragment_container_view)
            if(fragmentInstance is SignUpFragment){
                signupButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_color_stronger, theme))
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<LoginFragment>(R.id.access_fragment_container_view)
            }
            loginButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_color_lighter, theme))
        }

        signupButton.setOnClickListener {
            val fragmentInstance = supportFragmentManager.findFragmentById(R.id.access_fragment_container_view)
            if(fragmentInstance is LoginFragment){
                loginButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_color_stronger, theme))
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<SignUpFragment>(R.id.access_fragment_container_view)
            }
            signupButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_color_lighter, theme))
        }
    }
}