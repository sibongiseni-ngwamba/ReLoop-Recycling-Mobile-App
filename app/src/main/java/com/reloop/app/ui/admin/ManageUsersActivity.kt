package com.reloop.app.ui.admin

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reloop.app.R
import com.reloop.app.utils.UserAdapter
import com.reloop.app.viewmodel.AdminViewModel

class ManageUsersActivity : AppCompatActivity() {
    private val viewModel: AdminViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        findViewById<TextView>(R.id.titleText).text = "Manage Users"
        findViewById<TextView>(R.id.subtitleText).text = "Activate or deactivate local app users"
        val adapter = UserAdapter { viewModel.toggleUser(it) }
        findViewById<RecyclerView>(R.id.recyclerView).apply { layoutManager = LinearLayoutManager(this@ManageUsersActivity); this.adapter = adapter }
        viewModel.users.observe(this) { adapter.submit(it) }
    }
}
