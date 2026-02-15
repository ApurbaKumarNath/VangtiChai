package com.example.vangtichai

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Variable to store the user's input (e.g., "234")
    private var currentInput: String = ""

    // UI Components
    private lateinit var tvTotalAmount: TextView

    // CHANGED: We now have two text views for the results
    private lateinit var tvResultHigh: TextView
    private lateinit var tvResultLow: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize Views
        tvTotalAmount = findViewById(R.id.tvTotalAmount)

        // CHANGED: Initialize the split views
        tvResultHigh = findViewById(R.id.tvResultHigh)
        tvResultLow = findViewById(R.id.tvResultLow)

        // 2. Setup Number Buttons (0-9)
        val numberButtonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in numberButtonIds) {
            findViewById<Button>(id).setOnClickListener { view ->
                val button = view as Button
                // Add the digit to current string
                handleDigitInput(button.text.toString())
            }
        }

        // 3. Setup Clear Button
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            handleClear()
        }

        // 4. Restore State (Handle Rotation)
        if (savedInstanceState != null) {
            currentInput = savedInstanceState.getString("KEY_INPUT", "")
        }

        // 5. Update UI with current (or restored) data
        updateUI()
    }

    /**
     * Save the current input before the activity is destroyed (Rotation)
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("KEY_INPUT", currentInput)
    }

    private fun handleDigitInput(digit: String) {
        // Prevent leading zeros loop (e.g. 000)
        if (currentInput == "0") {
            currentInput = digit
        } else {
            // Limit length to prevent integer overflow (9 digits max)
            if (currentInput.length < 9) {
                currentInput += digit
            }
        }
        updateUI()
    }

    private fun handleClear() {
        currentInput = "0"
        updateUI()
    }

    private fun updateUI() {
        // Handle empty string case
        val amountStr = if (currentInput.isEmpty()) "0" else currentInput
        tvTotalAmount.text = "Taka: $amountStr"

        val amount = amountStr.toIntOrNull() ?: 0

        // Use our Logic Object
        val breakdown = CurrencyCalculator.calculateBreakdown(amount)

        // CHANGED: Logic to split the display into two strings
        val highDenoms = listOf(500, 100, 50, 20)
        val lowDenoms = listOf(10, 5, 2, 1)

        // 1. Build High Denominations String
        val sbHigh = StringBuilder()
        for (note in highDenoms) {
            val count = breakdown[note] ?: 0
            sbHigh.append("$note: $count\n")
        }
        // Trim removes the last new line character
        tvResultHigh.text = sbHigh.toString().trim()

        // 2. Build Low Denominations String
        val sbLow = StringBuilder()
        for (note in lowDenoms) {
            val count = breakdown[note] ?: 0
            sbLow.append("$note: $count\n")
        }
        tvResultLow.text = sbLow.toString().trim()
    }
}