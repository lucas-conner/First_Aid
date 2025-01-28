package com.example.pa4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmergencyHealthInformationActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextSex: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextMedications: EditText
    private lateinit var editTextMedicalConditions: EditText
    private lateinit var editTextAllergies: EditText
    private lateinit var editTextPhoneNumbers: EditText
    private lateinit var editTextInsuranceInfo: EditText

    private val database by lazy { AppDatabase.getDatabase(this).healthInfoDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_health_information)

        initializeFields()

        findViewById<Button>(R.id.buttonSaveInformation).setOnClickListener {
            saveInformation()
        }
    }

    private fun initializeFields() {
        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        editTextSex = findViewById(R.id.editTextSex)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextMedications = findViewById(R.id.editTextMedications)
        editTextMedicalConditions = findViewById(R.id.editTextMedicalConditions)
        editTextAllergies = findViewById(R.id.editTextAllergies)
        editTextPhoneNumbers = findViewById(R.id.editTextPhoneNumbers)
        editTextInsuranceInfo = findViewById(R.id.editTextInsuranceInfo)
    }

    private fun saveInformation() {
        val healthInfo = EmergencyHealthInfo(
            name = editTextName.text.toString(),
            age = editTextAge.text.toString(),
            sex = editTextSex.text.toString(),
            address = editTextAddress.text.toString(),
            medications = editTextMedications.text.toString(),
            medicalConditions = editTextMedicalConditions.text.toString(),
            allergies = editTextAllergies.text.toString(),
            phoneNumbers = editTextPhoneNumbers.text.toString(),
            insuranceInfo = editTextInsuranceInfo.text.toString()
        )

        // Launch coroutine to save information
        CoroutineScope(Dispatchers.IO).launch {
            database.insertHealthInfo(healthInfo)
        }
    }
}