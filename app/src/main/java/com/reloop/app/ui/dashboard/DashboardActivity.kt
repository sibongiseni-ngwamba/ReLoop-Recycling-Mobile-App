package com.reloop.app.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reloop.app.R
import com.reloop.app.ui.auth.LoginActivity
import com.reloop.app.ui.guidance.RecyclingGuidanceActivity
import com.reloop.app.ui.pickup.PickupHistoryActivity
import com.reloop.app.ui.pickup.SchedulePickupActivity
import com.reloop.app.ui.profile.NotificationsActivity
import com.reloop.app.ui.profile.ProfileActivity
import com.reloop.app.ui.rewards.RewardsActivity
import com.reloop.app.ui.scanner.WasteScannerActivity
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.DashboardViewModel

class DashboardActivity : AppCompatActivity() {
    private val viewModel: DashboardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val session = SessionManager(this)
        val userId = session.getUserId()
        viewModel.loadUser(userId) { findViewById<TextView>(R.id.welcomeText).text = "Welcome, ${it?.firstName ?: "Recycler"}" }
        viewModel.reward(userId).observe(this) { findViewById<TextView>(R.id.pointsText).text = "${it?.pointsBalance ?: 0} points" }
        viewModel.upcoming(userId).observe(this) { pickup ->
            findViewById<TextView>(R.id.upcomingText).text = pickup?.let { "Upcoming: ${it.wasteType} on ${it.scheduledDate} at ${it.scheduledTime}" } ?: "No upcoming pickup"
        }
        viewModel.unread(userId).observe(this) { count -> findViewById<Button>(R.id.notificationsButton).text = "Notifications ($count)" }
        nav(R.id.scheduleButton, SchedulePickupActivity::class.java)
        nav(R.id.historyButton, PickupHistoryActivity::class.java)
        nav(R.id.rewardsButton, RewardsActivity::class.java)
        nav(R.id.guidanceButton, RecyclingGuidanceActivity::class.java)
        nav(R.id.scannerButton, WasteScannerActivity::class.java)
        nav(R.id.profileButton, ProfileActivity::class.java)
        nav(R.id.notificationsButton, NotificationsActivity::class.java)
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            session.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    private fun nav(id: Int, target: Class<*>) = findViewById<Button>(id).setOnClickListener { startActivity(Intent(this, target)) }
}
