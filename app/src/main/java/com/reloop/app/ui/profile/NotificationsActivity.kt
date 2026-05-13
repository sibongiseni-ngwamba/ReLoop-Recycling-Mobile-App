package com.reloop.app.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reloop.app.R
import com.reloop.app.utils.NotificationAdapter
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.NotificationsViewModel

class NotificationsActivity : AppCompatActivity() {
    private val viewModel: NotificationsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val userID = SessionManager(this).getUserId()
        findViewById<TextView>(R.id.titleText).text = "Notifications"
        val subtitle = findViewById<TextView>(R.id.subtitleText)
        val empty = findViewById<TextView>(R.id.emptyText)
        empty.text = "No notifications yet."
        val adapter = NotificationAdapter { viewModel.markRead(it) }
        findViewById<RecyclerView>(R.id.recyclerView).apply { layoutManager = LinearLayoutManager(this@NotificationsActivity); this.adapter = adapter }
        viewModel.unread(userID).observe(this) { subtitle.text = "$it unread notification(s)" }
        viewModel.notifications(userID).observe(this) {
            adapter.submit(it)
            empty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
