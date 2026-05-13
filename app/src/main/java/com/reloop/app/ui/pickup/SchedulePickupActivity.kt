package com.reloop.app.ui.pickup

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reloop.app.R
import com.reloop.app.data.entities.Pickup
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.DashboardViewModel
import com.reloop.app.viewmodel.PickupViewModel
import java.util.Calendar

class SchedulePickupActivity : AppCompatActivity() {
    private val pickupViewModel: PickupViewModel by viewModels()
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private var date = ""
    private var time = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_pickup)
        val userID = SessionManager(this).getUserId()
        val address = findViewById<EditText>(R.id.addressEdit)
        val spinner = findViewById<Spinner>(R.id.wasteSpinner)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Plastic", "Glass", "Metal", "Paper", "E-Waste", "Mixed"))
        dashboardViewModel.loadUser(userID) { address.setText(it?.address.orEmpty()) }
        findViewById<Button>(R.id.dateButton).setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                date = "%04d-%02d-%02d".format(y, m + 1, d)
                findViewById<Button>(R.id.dateButton).text = date
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
        findViewById<Button>(R.id.timeButton).setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(this, { _, h, min ->
                time = "%02d:%02d".format(h, min)
                findViewById<Button>(R.id.timeButton).text = time
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            if (date.isBlank() || time.isBlank() || address.text.isBlank()) {
                Toast.makeText(this, "Choose date, time and address", Toast.LENGTH_SHORT).show()
            } else {
                pickupViewModel.schedule(Pickup(userID = userID, scheduledDate = date, scheduledTime = time, wasteType = spinner.selectedItem.toString(), address = address.text.toString())) {
                    Toast.makeText(this, "Pickup scheduled", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}
