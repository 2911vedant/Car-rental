
package com.example.carnow
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY_MS: Long = 2000 // 2 seconds delay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure you set the content view to your splash layout
        setContentView(R.layout.splash_screen_layout)

        // Use Handler to navigate after a delay
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if the user is already logged in (optional, but good practice)
            // For simplicity, we always go to AuthActivity for a fresh login.
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish() // Close the splash activity so the user can't go back to it
        }, SPLASH_DELAY_MS)
    }
}