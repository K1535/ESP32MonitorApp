package com.example.esp32monitorapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OnboardingStep2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_step2)

        // Obtener referencias a las vistas del layout XML

        val nextButton: Button = findViewById(R.id.next_button)
        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        val babyNameEditText: EditText = findViewById(R.id.baby_name_edit_text)

        // Configurar el listener para el clic del botón "Adelante"
        nextButton.setOnClickListener {
            // Obtener el texto del campo de entrada y limpiar espacios en blanco
            val babyName = babyNameEditText.text.toString().trim()

            if (babyName.isNotEmpty()) {
                // Si el nombre no está vacío, muestra un Toast de éxito
                val successMessage = "¡$babyName guardado correctamente!"
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()

                // Aquí puedes añadir la lógica para guardar el nombre en una base de datos
                // y navegar a la siguiente pantalla de la aplicación.
                val intent = Intent(this, OnboardingStep3Activity::class.java)
                intent.putExtra("BABY_NAME", babyName) // Pasa el nombre a la siguiente actividad

                startActivity(intent)
            } else {
                // Si el nombre está vacío, muestra un Toast de error
                Toast.makeText(this, "Por favor, introduce un nombre.", Toast.LENGTH_SHORT).show()
            }
            backArrow.setOnClickListener {
                finish()
            }
        }
    }
}
