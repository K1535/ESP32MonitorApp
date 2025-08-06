package com.example.esp32monitorapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class OnboardingStep1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_step1)

        val continueButton = findViewById<Button>(R.id.continue_button)
        val backArrow = findViewById<ImageView>(R.id.back_arrow)

        // Al hacer clic en el botón, se iniciará la siguiente actividad
        continueButton.setOnClickListener {
            // Reemplaza "OnboardingStep2Activity::class.java" con el nombre de tu siguiente actividad
            val intent = Intent(this, OnboardingStep2Activity::class.java)
            startActivity(intent)
        }

        // Al hacer clic en la flecha de regreso, se volverá a la pantalla anterior
        backArrow.setOnClickListener {
            finish()
        }
    }
}
