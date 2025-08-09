package com.example.esp32monitorapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logoImageView = findViewById<ImageView>(R.id.logo_splash)
        val taglineTextView = findViewById<TextView>(R.id.tagline_splash)

        // Carga la animación del logo
        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)

        // Carga la nueva animación para el texto
        val textAnimation = AnimationUtils.loadAnimation(this, R.anim.text_fade_in)

        // Asigna la animación al logo
        logoImageView.startAnimation(logoAnimation)

        // Escucha el final de la animación del logo
        logoAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                // Inicia la animación del texto solo después de que el logo ha terminado de moverse
                // Aseguramos que el TextView sea visible antes de animarlo
                taglineTextView.visibility = View.VISIBLE
                taglineTextView.startAnimation(textAnimation)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

        // Espera un momento y luego inicia la actividad de bienvenida
        val delayMillis = 2500L
        Handler(Looper.getMainLooper()).postDelayed({
            // Obtiene una referencia a SharedPreferences
            val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

            // Comprueba si el onboarding ha sido completado
            val isOnboardingComplete = sharedPref.getBoolean("is_onboarding_complete", false)

            if (isOnboardingComplete) {
                // Si el onboarding está completo, ve a la pantalla de opciones de administrador
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            } else {
                // Si no, ve a la primera pantalla de onboarding
                val intent = Intent(this, OnboardingStep1Activity::class.java)
                startActivity(intent)
            }
            finish() // Cierra la actividad de Splash para que el usuario no pueda volver a ella
        }, delayMillis) // Un retraso de 2.5 segundos para mostrar la animación completa
    }
}
