package com.example.esp32monitorapp // ¡IMPORTANTE: Este debe ser el nombre real de tu paquete!

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FinalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el diseño XML para esta actividad
        setContentView(R.layout.activity_final) // Asegúrate de que el nombre del archivo XML sea 'activity_final.xml'

        // Inicializa las vistas desde el XML
        val celebrationIcon: ImageView = findViewById(R.id.celebration_icon)
        val finalTextView: TextView = findViewById(R.id.final_text_view)
        val subtitleTextView: TextView = findViewById(R.id.subtitle_text_view)
        val goToMainButton: Button = findViewById(R.id.go_to_main_button)

        // --- Animaciones (opcional, pero mejora la experiencia) ---
        // Animación para el icono de celebración
        val iconScaleAnimation = ScaleAnimation(
            0.5f, 1.0f, 0.5f, 1.0f, // De la mitad de tamaño a tamaño completo
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // Pivote X (centro)
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f  // Pivote Y (centro)
        ).apply {
            duration = 800 // Duración de la animación en milisegundos
            startOffset = 200 // Retraso antes de que comience la animación
        }
        val iconAlphaAnimation = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 800
            startOffset = 200
        }
        val iconAnimationSet = AnimationSet(true).apply {
            addAnimation(iconScaleAnimation)
            addAnimation(iconAlphaAnimation)
        }
        celebrationIcon.startAnimation(iconAnimationSet)

        // Animación para el texto principal
        val textAlphaAnimation = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 1000 // Un poco más lenta
            startOffset = 500 // Retraso
        }
        finalTextView.startAnimation(textAlphaAnimation)

        // Animación para el subtítulo
        val subtitleAlphaAnimation = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 1000
            startOffset = 700
        }
        subtitleTextView.startAnimation(subtitleAlphaAnimation)


        // --- Lógica del botón ---
        // Configura el listener de clic para el botón "Finalizar"
        goToMainButton.setOnClickListener {
            // 1. Recuperar los datos del Intent que viene de OnboardingStep4Activity
            val nombreBebe = intent.getStringExtra("BABY_NAME") ?: "N/A"
            val fechaNacimientoBebe = intent.getStringExtra("BABY_BIRTH_DATE") ?: "N/A"
            val generoBebe = intent.getStringExtra("BABY_GENDER") ?: "niño" // Valor por defecto

            // 2. Guardar los datos del bebé en SharedPreferences para persistencia
            val sharedPref = getSharedPreferences("BabyInfo", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("BABY_NAME", nombreBebe)
                putString("BABY_BIRTH_DATE", fechaNacimientoBebe)
                putString("BABY_GENDER", generoBebe)
                apply()
            }

            // 3. Crear un nuevo Intent para ir a MainActivity
            val intentToMain = Intent(this, MainActivity::class.java)

            // Limpia la pila de actividades para que el usuario no pueda volver a las pantallas de onboarding
            intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentToMain)
            finish() // Finaliza esta actividad para que no se pueda volver a ella
        }
    }
}
