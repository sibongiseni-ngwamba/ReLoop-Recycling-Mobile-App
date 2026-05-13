package com.reloop.app.ui.admin

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reloop.app.R
import com.reloop.app.utils.PickupAdapter
import com.reloop.app.viewmodel.AdminViewModel

class ManagePickupsActivity : AppCompatActivity() {
    private val viewModel: AdminViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        findViewById<TextView>(R.id.titleText).text = "Manage Pickups"
        findViewById<TextView>(R.id.subtitleText).text = "Tap action to move pending to confirmed, then completed"
        val adapter = PickupAdapter(adminMode = true) { pickup, status -> viewModel.updatePickup(pickup, status) }
        findViewById<RecyclerView>(R.id.recyclerView).apply { layoutManager = LinearLayoutManager(this@ManagePickupsActivity); this.adapter = adapter }
        viewModel.allPickups.observe(this) { adapter.submit(it) }
    }
}
