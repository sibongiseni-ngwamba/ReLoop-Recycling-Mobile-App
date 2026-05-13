package com.reloop.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.reloop.app.R
import com.reloop.app.ui.admin.AdminDashboardActivity
import com.reloop.app.ui.dashboard.DashboardActivity
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.seed()
        lifecycleScope.launch {
            delay(800)
            val session = SessionManager(this@SplashActivity)
            val target = if (session.isLoggedIn() && session.getRole() == "admin") AdminDashboardActivity::class.java
            else if (session.isLoggedIn()) DashboardActivity::class.java
            else LoginActivity::class.java
            startActivity(Intent(this@SplashActivity, target))
            finish()
        }
    }
}
