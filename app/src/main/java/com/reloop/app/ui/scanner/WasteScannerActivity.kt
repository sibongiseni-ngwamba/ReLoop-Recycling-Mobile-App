package com.reloop.app.ui.scanner

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reloop.app.R
import com.reloop.app.ui.guidance.RecyclingGuidanceActivity
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.ScannerViewModel

class WasteScannerActivity : AppCompatActivity() {
    private val viewModel: ScannerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        val spinner = findViewById<Spinner>(R.id.categorySpinner)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Plastic PET", "Glass", "Paper/Cardboard", "Metal", "E-Waste"))
        val userID = SessionManager(this).getUserId()
        // The gallery/camera workflow is simulated by saving a classified WasteLog locally.
        findViewById<Button>(R.id.galleryButton).setOnClickListener { viewModel.classify(userID, spinner.selectedItem.toString()) }
        findViewById<Button>(R.id.randomButton).setOnClickListener { viewModel.classify(userID) }
        findViewById<Button>(R.id.guidanceButton).setOnClickListener { startActivity(Intent(this, RecyclingGuidanceActivity::class.java)) }
        viewModel.result.observe(this) {
            val parts = it.split("|")
            findViewById<TextView>(R.id.resultText).text = "Classification: ${parts[0]}\nConfidence: ${parts[1]}%\nSaved to waste log."
            Toast.makeText(this, "Waste log saved", Toast.LENGTH_SHORT).show()
        }
    }
}
