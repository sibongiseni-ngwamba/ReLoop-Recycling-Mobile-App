package com.reloop.app.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reloop.app.R
import com.reloop.app.ui.auth.LoginActivity
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.AdminViewModel

class AdminDashboardActivity : AppCompatActivity() {
    private val viewModel: AdminViewModel by viewModels()
    private val stats = mutableMapOf("users" to 0, "pickups" to 0, "pending" to 0, "completed" to 0, "redeemed" to 0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        viewModel.totalUsers.observe(this) { stats["users"] = it; render() }
        viewModel.totalPickups.observe(this) { stats["pickups"] = it; render() }
        viewModel.pendingPickups.observe(this) { stats["pending"] = it; render() }
        viewModel.completedPickups.observe(this) { stats["completed"] = it; render() }
        viewModel.totalRedemptions.observe(this) { stats["redeemed"] = it; render() }
        nav(R.id.usersButton, ManageUsersActivity::class.java)
        nav(R.id.pickupsButton, ManagePickupsActivity::class.java)
        nav(R.id.rewardsButton, ManageRewardsActivity::class.java)
        nav(R.id.reportsButton, ReportsActivity::class.java)
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            SessionManager(this).logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    private fun render() {
        findViewById<TextView>(R.id.statsText).text =
            "Total users: ${stats["users"]}\nTotal pickups: ${stats["pickups"]}\nPending pickups: ${stats["pending"]}\nCompleted pickups: ${stats["completed"]}\nRewards redeemed: ${stats["redeemed"]}"
    }
    private fun nav(id: Int, target: Class<*>) = findViewById<Button>(id).setOnClickListener { startActivity(Intent(this, target)) }
}
