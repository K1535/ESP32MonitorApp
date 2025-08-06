package com.example.esp32monitorapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class OnboardingStep4Activity : AppCompatActivity() {

    // Variable para almacenar el género seleccionado, inicializada como nula
    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_step4)

        // Referencias a las vistas del layout XML
        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        val maleButton = findViewById<CardView>(R.id.male_button)
        val femaleButton = findViewById<CardView>(R.id.female_button)
        val maleText = findViewById<TextView>(R.id.male_text)
        val femaleText = findViewById<TextView>(R.id.female_text)
        val skipButton = findViewById<TextView>(R.id.skip_button)
        val nextButton = findViewById<Button>(R.id.next_button)

        // --- Recuperar los datos del bebé de la actividad anterior ---
        val babyName = intent.getStringExtra("BABY_NAME") ?: "N/A"
        val babyBirthDate = intent.getStringExtra("BABY_BIRTH_DATE") ?: "N/A"

        // Listener para el botón "Niño"
        maleButton.setOnClickListener {
            selectedGender = "niño" // Establece el género como "niño" (en minúsculas para consistencia con JS)
            // Cambiar el estilo del botón seleccionado
            maleButton.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_blue))
            maleText.setTextColor(Color.WHITE)

            // Restablecer el estilo del otro botón
            femaleButton.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            femaleText.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        // Listener para el botón "Niña"
        femaleButton.setOnClickListener {
            selectedGender = "niña" // Establece el género como "niña" (en minúsculas para consistencia con JS)
            // Cambiar el estilo del botón seleccionado
            femaleButton.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_pink))
            femaleText.setTextColor(Color.WHITE)

            // Restablecer el estilo del otro botón
            maleButton.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            maleText.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        // Listener para el botón "Adelante"
        nextButton.setOnClickListener {
            if (selectedGender != null) {
                // Si se ha seleccionado un género, se pasa a la siguiente actividad
                Toast.makeText(this, "Género seleccionado: $selectedGender", Toast.LENGTH_SHORT).show()

                // Crear el Intent para FinalActivity
                val intent = Intent(this, FinalActivity::class.java)

                // Pasar todos los datos del bebé a FinalActivity
                intent.putExtra("BABY_NAME", babyName)
                intent.putExtra("BABY_BIRTH_DATE", babyBirthDate)
                intent.putExtra("BABY_GENDER", selectedGender) // Pasa el género seleccionado

                startActivity(intent)
                finish() // Finaliza esta actividad para que no se pueda volver a ella
            } else {
                Toast.makeText(this, "Por favor, selecciona un género.", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener para el botón "Omitir" (si el usuario no quiere seleccionar género)
        skipButton.setOnClickListener {
            // Si se omite, se pasa a la siguiente actividad con género por defecto o nulo
            Toast.makeText(this, "Género omitido.", Toast.LENGTH_SHORT).show()

            // Crear el Intent para FinalActivity
            val intent = Intent(this, FinalActivity::class.java)

            // Pasar los datos del bebé, con género como "N/A" o "niño" por defecto si se omite
            intent.putExtra("BABY_NAME", babyName)
            intent.putExtra("BABY_BIRTH_DATE", babyBirthDate)
            intent.putExtra("BABY_GENDER", "N/A") // Puedes usar "niño" como valor por defecto si lo prefieres

            startActivity(intent)
            finish() // Finaliza esta actividad
        }

        // Listener para la flecha de regreso
        backArrow.setOnClickListener {
            finish()
        }
    }
}
