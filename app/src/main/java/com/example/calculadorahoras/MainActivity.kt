package com.exemplo.calculadoradehoras

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import com.exemplo.calculadoradehoras.R
import kotlin.text.clear

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Aqui você garante que está chamando o layout correto

        val entradaEditText: EditText = findViewById(R.id.editTextEntrada)
        val saidaEditText: EditText = findViewById(R.id.editTextSaida)
        val resultadoTextView: TextView = findViewById(R.id.textViewResultado)
        val calcularButton: Button = findViewById(R.id.buttonCalcular)
        val limparButton: Button = findViewById(R.id.buttonLimpar)

        // Abrir TimePickerDialog ao clicar no EditText de Entrada
        entradaEditText.setOnClickListener {
            Log.d("MainActivity", "Entrada clicada")
            val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
                entradaEditText.setText(String.format("%02d:%02d", hourOfDay, minute))
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true)
            timePicker.show()
        }

        // Abrir TimePickerDialog ao clicar no EditText de Saída
        saidaEditText.setOnClickListener {
            Log.d("MainActivity", "Saída clicada")
            val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
                saidaEditText.setText(String.format("%02d:%02d", hourOfDay, minute))
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true)
            timePicker.show()
        }

        // Lógica para calcular horários ao clicar no botão
        calcularButton.setOnClickListener {
            Log.d("MainActivity", "Botão calcular clicado")
            val entradaTexto = entradaEditText.text.toString()
            val saidaTexto = saidaEditText.text.toString()

            if (entradaTexto.isNotEmpty() && saidaTexto.isEmpty()) {
                calcularSaida(entradaTexto, resultadoTextView)
            } else if (saidaTexto.isNotEmpty() && entradaTexto.isEmpty()) {
                calcularEntrada(saidaTexto, resultadoTextView)
            } else {
                Toast.makeText(this, "Por favor, insira apenas um dos horários.", Toast.LENGTH_SHORT).show()
            }
        }

        limparButton.setOnClickListener {
            entradaEditText.text.clear()
            saidaEditText.text.clear()
            resultadoTextView.text = ""
        }
    }

    private fun calcularSaida(entradaTexto: String, resultadoTextView: TextView) {
        try {
            val horarioEntrada = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(entradaTexto)
            val calendario = Calendar.getInstance().apply {
                time = horarioEntrada!!
            }

            // Defina a jornada de trabalho sem descontar o horário de almoço
            val jornadaHoras = 9 // Horas
            val jornadaMinutos = 50 // Minutos

            // Adiciona a jornada diretamente
            calendario.add(Calendar.HOUR_OF_DAY, jornadaHoras)
            calendario.add(Calendar.MINUTE, jornadaMinutos)

            val horarioSaida = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendario.time)
            resultadoTextView.text = "Sua saída será às: $horarioSaida"
        } catch (e: Exception) {
            Toast.makeText(this, "Formato de horário inválido. Use HH:mm.", Toast.LENGTH_SHORT).show()
            Log.e("MainActivity", "Erro ao calcular saída", e)
        }
    }

    private fun calcularEntrada(saidaTexto: String, resultadoTextView: TextView) {
        try {
            val horarioSaida = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(saidaTexto)
            val calendario = Calendar.getInstance().apply {
                time = horarioSaida!!
            }

            // Defina a jornada de trabalho sem descontar o horário de almoço
            val jornadaHoras = 9 // Horas
            val jornadaMinutos = 50 // Minutos

            // Subtrai a jornada diretamente
            calendario.add(Calendar.HOUR_OF_DAY, -jornadaHoras)
            calendario.add(Calendar.MINUTE, -jornadaMinutos)

            val horarioEntrada = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendario.time)
            resultadoTextView.text = "Sua entrada deve ser às: $horarioEntrada"
        } catch (e: Exception) {
            Toast.makeText(this, "Por favor, insira apenas um dos horários.", Toast.LENGTH_SHORT).show()
            Log.e("MainActivity", "Erro ao calcular entrada", e)
        }
    }
}
