package com.reloop.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reloop.app.R
import com.reloop.app.ui.admin.AdminDashboardActivity
import com.reloop.app.ui.dashboard.DashboardActivity
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel.seed()
        val email = findViewById<EditText>(R.id.emailEdit)
        val password = findViewById<EditText>(R.id.passwordEdit)
        findViewById<Button>(R.id.registerButton).setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        findViewById<Button>(R.id.loginButton).setOnClickListener {
            if (email.text.isBlank() || password.text.isBlank()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email.text.toString(), password.text.toString())
            }
        }
        viewModel.loginResult.observe(this) { user ->
            if (user == null) {
                Toast.makeText(this, "Invalid login or inactive account", Toast.LENGTH_SHORT).show()
            } else {
                SessionManager(this).saveSession(user.userID, user.role)
                startActivity(Intent(this, if (user.role == "admin") AdminDashboardActivity::class.java else DashboardActivity::class.java))
                finish()
            }
        }
    }
}
