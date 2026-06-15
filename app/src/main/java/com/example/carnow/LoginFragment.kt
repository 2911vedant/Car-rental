package com.example.carnow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginFragment : Fragment() {

    private lateinit var dbHelper: SQLiteDatabaseHelper
    private val PREF_USER_EMAIL = "logged_in_user_email"

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = SQLiteDatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val etEmail = view.findViewById<EditText>(R.id.et_login_email)
        val etPassword = view.findViewById<EditText>(R.id.et_login_password)
        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val tvSignup = view.findViewById<TextView>(R.id.signup)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate user in database
            if (dbHelper.checkUser(email, password)) {
                // ✅ Save user session safely before moving to HomeActivity
                val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString(PREF_USER_EMAIL, email)
                    commit() // commit ensures it’s saved before starting HomeActivity
                }

                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()

                // ✅ Start HomeActivity and close AuthActivity
                val intent = Intent(requireActivity(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(context, "Invalid email or password.", Toast.LENGTH_LONG).show()
            }
        }

        tvSignup.setOnClickListener {
            (activity as? AuthActivity)?.showSignup()
        }

        return view
    }
}
