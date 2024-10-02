package com.example.emicalculator

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

//        setSupportActionBar(binding.toolbar)

//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }

        fun calculateEMI(loanAmount: Double, interestRate: Double, loanTenure: Int): Double {
            // Convert annual interest rate to monthly and decimal form
            val monthlyRate = interestRate / 12 / 100
            // Calculate (1 + r)^n
            val factor = (1 + monthlyRate).pow(loanTenure.toDouble())
            // Calculate EMI using the formula
            val emi = (loanAmount * monthlyRate * factor) / (factor - 1)

            return emi
        }

        val loanAmount = findViewById<EditText>(R.id.loan_amount)
        val interestRate = findViewById<EditText>(R.id.interest_rate)
        val loanTenure = findViewById<EditText>(R.id.tenure)
        val calculateButton = findViewById<Button>(R.id.buttonCalculateEMI)
        val resultTv = findViewById<TextView>(R.id.textViewEMIResult)
        val resetButton = findViewById<Button>(R.id.button_reset)

        calculateButton.setOnClickListener() {
            val loanDouble = loanAmount.text.toString().toDoubleOrNull()
            val interestDouble = interestRate.text.toString().toDoubleOrNull()
            val loanInt = loanTenure.text.toString().toIntOrNull()
            println(loanDouble)
            println(interestDouble)
            println(loanInt)
            if(loanDouble != null && interestDouble != null && loanInt != null) {
                val emiResult = calculateEMI(loanDouble, interestDouble, loanInt)
                resultTv.text = emiResult.toString()
            } else {
                Toast.makeText(this, "Please input the right amount", Toast.LENGTH_SHORT).show()
            }
        }
        resetButton.setOnClickListener {
            loanAmount.text.clear()
            interestRate.text.clear()
            loanTenure.text.clear()
            Toast.makeText(this, "Success reset", Toast.LENGTH_SHORT).show()
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