package com.example.emicalculator

import java.text.DecimalFormat
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.emicalculator.databinding.ActivityMainBinding
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resetButton = binding.buttonReset
        val loanAmount = binding.editTextLoanAmount
        val interestRate = binding.editTextInterestRate
        val loanTenure = binding.editTextTenure

        binding.buttonCalculateEMI.setOnClickListener { performCalculation() }
        resetButton.setOnClickListener {
            loanAmount.text.clear()
            interestRate.text.clear()
            loanTenure.text.clear()
            Toast.makeText(this, "Success reset", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performCalculation() {
        val loanAmountStr = binding.editTextLoanAmount.text.toString()
        val interestRateStr = binding.editTextInterestRate.text.toString()
        val loanTenureStr = binding.editTextTenure.text.toString()

        if (loanAmountStr.isEmpty() || interestRateStr.isEmpty() || loanTenureStr.isEmpty()) {
            Toast.makeText(this, "Fields Cannot Be Empty", Toast.LENGTH_SHORT).show()
            return
        }

        val loanAmount = loanAmountStr.toString().toDoubleOrNull()
        val interestRate = interestRateStr.toString().toDoubleOrNull()
        val loanTenure = loanTenureStr.toString().toIntOrNull()

        if (loanAmount == null || interestRate == null || loanTenure == null) {
            Toast.makeText(this, "Invalid Input!", Toast.LENGTH_SHORT).show()
            return
        }

        val monthlyInterestRate = interestRate / (12 * 100)
        val emi = calculateEMI(loanAmount, monthlyInterestRate, loanTenure)

        val formatOutput = DecimalFormat("#,##0.00").format(emi)
        binding.textViewEMIResult.text = "Your EMI is: $formatOutput"
    }

    private fun calculateEMI(principal: Double, rate: Double, months: Int): Double {
        return if (rate == 0.0) {
            principal / months
        } else {
            val factor = (1 + rate).pow(months)
            (principal * rate * factor) / (factor - 1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}