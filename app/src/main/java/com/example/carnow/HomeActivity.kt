package com.example.carnow

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteDatabaseHelper
    private var currentUserEmail: String? = null
    private val PREF_USER_EMAIL = "logged_in_user_email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        dbHelper = SQLiteDatabaseHelper(this)

        // Retrieve the logged-in user's email from SharedPreferences
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        currentUserEmail = sharedPref.getString(PREF_USER_EMAIL, null)

        val tvWelcome = findViewById<TextView>(R.id.tv_welcome_user)
        val btnChangePassword = findViewById<Button>(R.id.btn_change_password)
        val btnLogout = findViewById<Button>(R.id.btn_logout)

        val etCurrentPass = findViewById<EditText>(R.id.et_change_password_current)
        val etNewPass = findViewById<EditText>(R.id.et_change_password_new)
        val etConfirmNewPass = findViewById<EditText>(R.id.et_change_password_confirm)

        // 🔒 If user email not found (not logged in), redirect to Auth screen
        if (currentUserEmail.isNullOrEmpty()) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        // Show welcome message safely
        tvWelcome.text = "Hello, $currentUserEmail!"

        // ✅ Change Password Logic
        btnChangePassword.setOnClickListener {
            val currentPass = etCurrentPass.text.toString().trim()
            val newPass = etNewPass.text.toString().trim()
            val confirmPass = etConfirmNewPass.text.toString().trim()

            if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "All password fields are required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPass != confirmPass) {
                Toast.makeText(this, "New passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Only continue if user email is available
            val email = currentUserEmail
            if (email != null) {
                // Check if current password is correct
                if (dbHelper.checkUser(email, currentPass)) {
                    val isUpdated = dbHelper.updatePassword(email, newPass)
                    if (isUpdated) {
                        Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_LONG).show()
                        etCurrentPass.setText("")
                        etNewPass.setText("")
                        etConfirmNewPass.setText("")
                    } else {
                        Toast.makeText(this, "Error updating password.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Incorrect current password.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ Logout Logic
        btnLogout.setOnClickListener {
            // Clear logged-in user
            with(sharedPref.edit()) {
                remove(PREF_USER_EMAIL)
                apply()
            }
            Toast.makeText(this, "Logged out.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
