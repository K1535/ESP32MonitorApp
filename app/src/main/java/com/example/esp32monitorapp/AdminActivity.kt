package com.example.esp32monitorapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val newAppButton: Button = findViewById(R.id.new_app_button)
        val continueButton: Button = findViewById(R.id.continue_button)

        newAppButton.setOnClickListener {
            // Llama a la función para borrar datos y navegar al onboarding
            resetAppAndGoToOnboarding()
        }

        continueButton.setOnClickListener {
            // Navega directamente a la MainActivity (el WebView)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun resetAppAndGoToOnboarding() {
        // Borra los datos del bebé
        val babyPref = getSharedPreferences("BabyInfo", Context.MODE_PRIVATE)
        with(babyPref.edit()) {
            clear()
            apply()
        }
        // También borra la bandera que indica que el onboarding está completo
        val onboardingPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(onboardingPref.edit()) {
            putBoolean("is_onboarding_complete", false)
            apply()
        }
        // Navega de vuelta a la primera pantalla de onboarding
        val intent = Intent(this, OnboardingStep1Activity::class.java)
        startActivity(intent)
        finish()
    }
}
