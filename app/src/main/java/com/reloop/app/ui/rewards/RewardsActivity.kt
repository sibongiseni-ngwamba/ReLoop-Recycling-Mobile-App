package com.reloop.app.ui.rewards

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reloop.app.R
import com.reloop.app.utils.RedemptionAdapter
import com.reloop.app.utils.RewardItemAdapter
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.RewardsViewModel

class RewardsActivity : AppCompatActivity() {
    private val viewModel: RewardsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)
        val userID = SessionManager(this).getUserId()
        val itemAdapter = RewardItemAdapter { viewModel.redeem(userID, it.rewardItemID) }
        val historyAdapter = RedemptionAdapter()
        findViewById<RecyclerView>(R.id.rewardRecycler).apply { layoutManager = LinearLayoutManager(this@RewardsActivity); adapter = itemAdapter }
        findViewById<RecyclerView>(R.id.historyRecycler).apply { layoutManager = LinearLayoutManager(this@RewardsActivity); adapter = historyAdapter }
        viewModel.reward(userID).observe(this) { findViewById<TextView>(R.id.pointsText).text = "Balance: ${it?.pointsBalance ?: 0} points" }
        viewModel.items.observe(this) { itemAdapter.submit(it) }
        viewModel.redemptions(userID).observe(this) { historyAdapter.submit(it) }
        viewModel.message.observe(this) { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
    }
}
