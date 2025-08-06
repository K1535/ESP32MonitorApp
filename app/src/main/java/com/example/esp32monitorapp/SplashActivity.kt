package com.example.esp32monitorapp

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
            val intent = Intent(this, OnboardingStep1Activity::class.java)
            startActivity(intent)
            finish()
        }, delayMillis)
    }
}
