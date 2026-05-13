package com.reloop.app.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reloop.app.R
import com.reloop.app.data.entities.User
import com.reloop.app.utils.SessionManager
import com.reloop.app.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private val viewModel: ProfileViewModel by viewModels()
    private var loaded: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val userID = SessionManager(this).getUserId()
        val first = findViewById<EditText>(R.id.firstNameEdit)
        val last = findViewById<EditText>(R.id.lastNameEdit)
        val email = findViewById<EditText>(R.id.emailEdit)
        val phone = findViewById<EditText>(R.id.phoneEdit)
        val address = findViewById<EditText>(R.id.addressEdit)
        val password = findViewById<EditText>(R.id.passwordEdit)
        viewModel.load(userID)
        viewModel.user.observe(this) { user ->
            loaded = user
            first.setText(user?.firstName)
            last.setText(user?.lastName)
            email.setText(user?.email)
            phone.setText(user?.phoneNumber)
            address.setText(user?.address)
        }
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val user = loaded ?: return@setOnClickListener
            val updated = user.copy(
                firstName = first.text.toString(),
                lastName = last.text.toString(),
                phoneNumber = phone.text.toString(),
                address = address.text.toString(),
                password = if (password.text.isBlank()) user.password else password.text.toString()
            )
            viewModel.update(updated) {
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
