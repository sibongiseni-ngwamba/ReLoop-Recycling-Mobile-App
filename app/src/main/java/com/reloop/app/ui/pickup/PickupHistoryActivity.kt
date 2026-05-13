package com.reloop.app.ui.pickup

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reloop.app.R
import com.reloop.app.utils.PickupAdapter
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.PickupViewModel

class PickupHistoryActivity : AppCompatActivity() {
    private val viewModel: PickupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        findViewById<TextView>(R.id.titleText).text = "Pickup History"
        findViewById<TextView>(R.id.subtitleText).text = "Your scheduled, confirmed and completed pickups"
        val empty = findViewById<TextView>(R.id.emptyText)
        empty.text = "No pickups yet. Schedule your first recycling pickup from the dashboard."
        val adapter = PickupAdapter()
        findViewById<RecyclerView>(R.id.recyclerView).apply { layoutManager = LinearLayoutManager(this@PickupHistoryActivity); this.adapter = adapter }
        viewModel.pickups(SessionManager(this).getUserId()).observe(this) {
            adapter.submit(it)
            empty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
