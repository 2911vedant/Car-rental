package com.example.carnow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // ✅ Step 1: Check if user is already logged in
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("logged_in_user_email", null)

        if (email != null) {
            // If user is already logged in, go directly to HomeActivity
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        // ✅ Step 2: Load LoginFragment as default (if not already restored)
        if (savedInstanceState == null) {
            showLogin()
        }
    }

    // ✅ Function to show LoginFragment
    fun showLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_fragment_container, LoginFragment.newInstance())
            .commit()
    }

    // ✅ Function to show SignupFragment
    fun showSignup() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_fragment_container, SignupFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}
