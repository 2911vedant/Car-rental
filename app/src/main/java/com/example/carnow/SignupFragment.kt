package com.example.carnow


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignupFragment : Fragment() {

    private lateinit var dbHelper:SQLiteDatabaseHelper

    companion object {
        fun newInstance() = SignupFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = SQLiteDatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.signup_fragment, container, false)
        val etEmail = view.findViewById<EditText>(R.id.et_signup_email)
        val etPassword = view.findViewById<EditText>(R.id.et_signup_password)
        val etConfirmPassword = view.findViewById<EditText>(R.id.et_signup_confirm_password)
        val btnSignup = view.findViewById<Button>(R.id.btn_signup)
        val tvLogin = view.findViewById<TextView>(R.id.tv_switch_to_login)

        btnSignup.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // In a real app, hash the password (SHA-256 recommended)
            // For this basic example, we use the raw password as the "hash"
            val isSuccess = dbHelper.addUser(email, password)

            if (isSuccess) {
                Toast.makeText(context, "Account created successfully! Please log in.", Toast.LENGTH_LONG).show()
                // Navigate back to the Login fragment
                (activity as? AuthActivity)?.showLogin()
            } else {
                Toast.makeText(context, "Registration failed. Email might already be in use.", Toast.LENGTH_LONG).show()
            }
        }

        tvLogin.setOnClickListener {
            // Tell the parent activity (AuthActivity) to switch back to the Login fragment
            (activity as? AuthActivity)?.showLogin()
        }

        return view
    }
}