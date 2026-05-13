package com.reloop.app.ui.admin

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reloop.app.R
import com.reloop.app.viewmodel.AdminViewModel

class ReportsActivity : AppCompatActivity() {
    private val viewModel: AdminViewModel by viewModels()
    private val values = mutableMapOf<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        viewModel.totalPickups.observe(this) { values["Total pickups"] = "$it"; render() }
        viewModel.completedPickups.observe(this) { values["Completed pickups"] = "$it"; render() }
        viewModel.totalKg.observe(this) { values["Kg recycled"] = "%.2f kg".format(it ?: 0.0); render() }
        viewModel.totalPoints.observe(this) { values["Reward points awarded"] = "${it ?: 0}"; render() }
        viewModel.commonWaste.observe(this) { values["Most common waste type"] = it ?: "None yet"; render() }
    }
    private fun render() {
        findViewById<TextView>(R.id.reportText).text = values.entries.joinToString("\n\n") { "${it.key}: ${it.value}" }
    }
}
