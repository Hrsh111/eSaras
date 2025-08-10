package com.example.esaras.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.esaras.R

class SignIn : AppCompatActivity() {
    private lateinit var emailPhoneEditText: EditText
    private lateinit var generateOtpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Initialize UI elements
        emailPhoneEditText = findViewById(R.id.emailPhoneEditText)
        generateOtpButton = findViewById(R.id.generateOtpButton)

        // Set click listener for the generate OTP button
        generateOtpButton.setOnClickListener {
            val emailOrPhone = emailPhoneEditText.text.toString()
            if (emailOrPhone.isNotEmpty()) {
                // Handle OTP generation logic here
                // For example, send OTP to the provided email/phone
            } else {
                // Show an error message
                emailPhoneEditText.error = "Please enter your email or phone number"
            }
        }
    }
}
