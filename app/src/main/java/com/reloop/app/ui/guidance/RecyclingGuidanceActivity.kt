package com.reloop.app.ui.guidance

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reloop.app.R
import com.reloop.app.utils.GuidanceAdapter
import com.reloop.app.viewmodel.GuidanceViewModel

class RecyclingGuidanceActivity : AppCompatActivity() {
    private val viewModel: GuidanceViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        findViewById<TextView>(R.id.titleText).text = "Recycling Guidance"
        findViewById<TextView>(R.id.subtitleText).text = "Tap a category to view simple preparation instructions"
        val adapter = GuidanceAdapter { category ->
            viewModel.guidanceFor(category.wasteCategoryID).observe(this) { guidance ->
                if (guidance != null) AlertDialog.Builder(this).setTitle(guidance.title).setMessage(guidance.content).setPositiveButton("OK", null).show()
            }
        }
        findViewById<RecyclerView>(R.id.recyclerView).apply { layoutManager = LinearLayoutManager(this@RecyclingGuidanceActivity); this.adapter = adapter }
        viewModel.categories.observe(this) { adapter.submit(it) }
    }
}
