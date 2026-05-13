package com.reloop.app.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reloop.app.R
import com.reloop.app.data.entities.User
import com.reloop.app.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val first = findViewById<EditText>(R.id.firstNameEdit)
        val last = findViewById<EditText>(R.id.lastNameEdit)
        val email = findViewById<EditText>(R.id.emailEdit)
        val pass = findViewById<EditText>(R.id.passwordEdit)
        val confirm = findViewById<EditText>(R.id.confirmEdit)
        val phone = findViewById<EditText>(R.id.phoneEdit)
        val address = findViewById<EditText>(R.id.addressEdit)
        findViewById<Button>(R.id.createButton).setOnClickListener {
            val fields = listOf(first, last, email, pass, confirm, phone, address)
            if (fields.any { it.text.isBlank() }) {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show()
            } else if (pass.text.toString() != confirm.text.toString()) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(
                    firstName = first.text.toString().trim(),
                    lastName = last.text.toString().trim(),
                    email = email.text.toString().trim(),
                    password = pass.text.toString(),
                    phoneNumber = phone.text.toString().trim(),
                    address = address.text.toString().trim()
                )
                viewModel.register(user) {
                    Toast.makeText(this, "Account created. Please login.", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
        viewModel.message.observe(this) { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
    }
}
