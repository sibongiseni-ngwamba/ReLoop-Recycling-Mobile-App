package com.reloop.app.ui.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reloop.app.R
import com.reloop.app.data.entities.RewardItem
import com.reloop.app.utils.RewardItemAdapter
import com.reloop.app.viewmodel.AdminViewModel

class ManageRewardsActivity : AppCompatActivity() {
    private val viewModel: AdminViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_rewards)
        val name = findViewById<EditText>(R.id.nameEdit)
        val desc = findViewById<EditText>(R.id.descriptionEdit)
        val cost = findViewById<EditText>(R.id.costEdit)
        val adapter = RewardItemAdapter { item -> viewModel.saveReward(item.copy(isActive = !item.isActive)) }
        findViewById<RecyclerView>(R.id.recyclerView).apply { layoutManager = LinearLayoutManager(this@ManageRewardsActivity); this.adapter = adapter }
        viewModel.rewardItems.observe(this) { adapter.submit(it) }
        findViewById<Button>(R.id.addButton).setOnClickListener {
            if (name.text.isBlank() || desc.text.isBlank() || cost.text.isBlank()) {
                Toast.makeText(this, "Complete all reward fields", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.saveReward(RewardItem(itemName = name.text.toString(), description = desc.text.toString(), pointsCost = cost.text.toString().toInt()))
                name.text.clear(); desc.text.clear(); cost.text.clear()
                Toast.makeText(this, "Reward item saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
