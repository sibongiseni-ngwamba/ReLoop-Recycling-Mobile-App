package com.reloop.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.reloop.app.R
import com.reloop.app.ui.admin.AdminDashboardActivity
import com.reloop.app.ui.dashboard.DashboardActivity
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.AuthViewModel

class WelcomeActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.seed()

        val session = SessionManager(this)
        if (session.isLoggedIn()) {
            startActivity(Intent(this, if (session.getRole() == "admin") AdminDashboardActivity::class.java else DashboardActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_welcome)

        findViewById<MaterialButton>(R.id.loginButton).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        findViewById<MaterialButton>(R.id.registerButton).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
