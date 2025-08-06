package com.example.esp32monitorapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OnboardingStep3Activity : AppCompatActivity() {

    private lateinit var selectedDateTextView: TextView
    private lateinit var babyAgeTextView: TextView
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_step3)

        // Obtener referencias a las vistas del layout XML
        val nextButton = findViewById<Button>(R.id.next_button)
        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        babyAgeTextView = findViewById<TextView>(R.id.baby_age_months)
        val dateSelectorLayout = findViewById<LinearLayout>(R.id.date_selector_layout)
        selectedDateTextView = findViewById<TextView>(R.id.selected_date_text_view)

        // --- Recuperar el nombre del bebé de la actividad anterior ---
        val babyName = intent.getStringExtra("BABY_NAME") ?: "N/A"

        // Configurar el listener para el selector de fecha
        dateSelectorLayout.setOnClickListener {
            // Obtener el año, mes y día actuales para inicializar el DatePickerDialog
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Crear y mostrar el DatePickerDialog con un tema personalizado
            // El estilo R.style.AppTheme_DatePickerDialog se define en themes.xml
            val datePickerDialog = DatePickerDialog(
                this,
                R.style.AppTheme_DatePickerDialog, // Asegúrate de que este estilo exista en tu themes.xml
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Cuando se selecciona una fecha, actualizar el calendario y la interfaz de usuario
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    updateDateInView()
                    calculateBabyAge()
                },
                year, month, day
            )

            // Restringir el DatePicker para que no se puedan seleccionar fechas futuras
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

            datePickerDialog.show()
        }

        // Al hacer clic en el botón "Adelante", se iniciará la siguiente actividad
        nextButton.setOnClickListener {
            val selectedDate = selectedDateTextView.text.toString()
            // Se valida que el usuario haya seleccionado una fecha antes de continuar
            if (selectedDate == "Selecciona la fecha") {
                Toast.makeText(this, "Por favor, selecciona una fecha.", Toast.LENGTH_SHORT).show()
            } else {
                // Pasar a la siguiente actividad, enviando el nombre y la fecha de nacimiento
                val intent = Intent(this, OnboardingStep4Activity::class.java)
                intent.putExtra("BABY_NAME", babyName) // Pasa el nombre que recibiste
                intent.putExtra("BABY_BIRTH_DATE", selectedDate) // Pasa la fecha de nacimiento seleccionada
                startActivity(intent)

                Toast.makeText(this, "Fecha de nacimiento guardada: $selectedDate", Toast.LENGTH_SHORT).show()
            }
        }

        // Al hacer clic en la flecha de regreso, se vuelve a la pantalla anterior
        backArrow.setOnClickListener {
            finish()
        }
    }

    // Actualiza la TextView con la fecha seleccionada
    private fun updateDateInView() {
        val dateFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        selectedDateTextView.text = sdf.format(calendar.time)
    }

    // Calcula la edad del bebé en meses y actualiza la TextView correspondiente
    private fun calculateBabyAge() {
        val birthDate = calendar.timeInMillis
        val currentDate = System.currentTimeMillis()

        val birthCalendar = Calendar.getInstance().apply { timeInMillis = birthDate }
        val currentCalendar = Calendar.getInstance().apply { timeInMillis = currentDate }

        var years = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
        var months = currentCalendar.get(Calendar.MONTH) - birthCalendar.get(Calendar.MONTH)

        if (currentCalendar.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH)) {
            months--
        }

        val totalMonths = years * 12 + months
        babyAgeTextView.text = if (totalMonths >= 0) totalMonths.toString() else "0"
    }
}
