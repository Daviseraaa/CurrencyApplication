package com.example.currencyapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextSourceAmount: EditText
    private lateinit var editTextTargetAmount: EditText
    private lateinit var spinnerSourceCurrency: Spinner
    private lateinit var spinnerTargetCurrency: Spinner
    private val exchangeRates = hashMapOf("USD" to 1.0, "VND" to 24000.0, "EUR" to 0.85, "JPY" to 110.0)
    private var isSourceFocused = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextSourceAmount = findViewById(R.id.editTextSourceAmount)
        editTextTargetAmount = findViewById(R.id.editTextTargetAmount)
        spinnerSourceCurrency = findViewById(R.id.spinnerSourceCurrency)
        spinnerTargetCurrency = findViewById(R.id.spinnerTargetCurrency)

        val currencies = arrayOf("USD", "VND", "EUR", "JPY")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSourceCurrency.adapter = adapter
        spinnerTargetCurrency.adapter = adapter

        editTextSourceAmount.setOnFocusChangeListener { _, hasFocus -> isSourceFocused = hasFocus }

        editTextSourceAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isSourceFocused) updateConversion()
            }
        })

        spinnerSourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isSourceFocused) updateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerTargetCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isSourceFocused) updateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateConversion() {
        try {
            val sourceAmount = editTextSourceAmount.text.toString().toDouble()
            val sourceRate = exchangeRates[spinnerSourceCurrency.selectedItem.toString()] ?: 1.0
            val targetRate = exchangeRates[spinnerTargetCurrency.selectedItem.toString()] ?: 1.0
            val convertedAmount = (sourceAmount / sourceRate) * targetRate
            editTextTargetAmount.setText(convertedAmount.toString())
        } catch (e: NumberFormatException) {
            editTextTargetAmount.setText("")
        }
    }
}
