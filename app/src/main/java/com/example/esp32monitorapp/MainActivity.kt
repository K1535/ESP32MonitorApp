package com.example.esp32monitorapp // ¡IMPORTANTE: Este debe ser el nombre real de tu paquete!

import android.content.Context
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)

        // --- Configuración de WebSettings para permitir la comunicación ---
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // Habilita JavaScript en la WebView
        webSettings.domStorageEnabled = true // Habilita el almacenamiento DOM (necesario para algunas funciones web)

        // *** CRÍTICO para permitir solicitudes HTTP desde archivos locales ***
        // Estas dos líneas son fundamentales para que el HTML cargado desde 'file:///android_asset/'
        // pueda hacer peticiones de red (HTTP) a tu ESP32.
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.allowFileAccessFromFileURLs = true

        // Configura un WebViewClient para que los enlaces se abran dentro de la app
        webView.webViewClient = WebViewClient()

        // Añade la interfaz JavaScript para comunicación bidireccional
        // El nombre "Android" es cómo tu JavaScript accederá a esta interfaz (ej. window.Android.saveBabyInfo)
        webView.addJavascriptInterface(WebAppInterface(this), "Android")

        // --- Carga de datos del bebé (desde SharedPreferences o Intent) ---
        val sharedPref = getSharedPreferences("BabyInfo", Context.MODE_PRIVATE)

        // Intentamos cargar de SharedPreferences primero
        var babyName = sharedPref.getString("BABY_NAME", null)
        var babyBirthDate = sharedPref.getString("BABY_BIRTH_DATE", null)
        var babyGender = sharedPref.getString("BABY_GENDER", null)

        // Si no hay datos en SharedPreferences, los obtenemos del Intent (primera vez después de onboarding)
        if (babyName == null || babyBirthDate == null || babyGender == null) {
            babyName = intent.getStringExtra("BABY_NAME") ?: "N/A"
            babyBirthDate = intent.getStringExtra("BABY_BIRTH_DATE") ?: "N/A"
            babyGender = intent.getStringExtra("BABY_GENDER") ?: "niño" // 'niño' por defecto
            // Guardamos estos datos iniciales en SharedPreferences
            with(sharedPref.edit()) {
                putString("BABY_NAME", babyName)
                putString("BABY_BIRTH_DATE", babyBirthDate)
                putString("BABY_GENDER", babyGender)
                apply()
            }
        }

        // Construye la URL con los parámetros de consulta (codificados)
        val encodedBabyName = URLEncoder.encode(babyName, "UTF-8")
        val encodedBabyBirthDate = URLEncoder.encode(babyBirthDate, "UTF-8")
        val encodedBabyGender = URLEncoder.encode(babyGender, "UTF-8")

        val url = "file:///android_asset/index.html?" +
                "name=${encodedBabyName}&" +
                "date=${encodedBabyBirthDate}&" +
                "gender=${encodedBabyGender}"

        webView.loadUrl(url)
    }

    // Clase de interfaz JavaScript para permitir que JS llame a funciones Kotlin
    class WebAppInterface(private val mContext: Context) {

        @JavascriptInterface // ¡IMPORTANTE! Esta anotación es necesaria para que JS pueda llamar a esta función
        fun saveBabyInfo(name: String, birthDate: String, gender: String) {
            val sharedPref = mContext.getSharedPreferences("BabyInfo", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("BABY_NAME", name)
                putString("BABY_BIRTH_DATE", birthDate)
                putString("BABY_GENDER", gender)
                apply()
            }
            // Puedes mostrar un Toast para confirmar que se guardó.
            // Los Toasts siempre se ejecutan en el hilo principal de la UI.
            Toast.makeText(mContext, "Datos del bebé guardados: $name ($gender)", Toast.LENGTH_SHORT).show()
        }
    }

    // Maneja el botón de retroceso para navegar dentro del WebView
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
