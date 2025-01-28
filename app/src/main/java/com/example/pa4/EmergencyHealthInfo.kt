package com.example.pa4

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_health_info")
data class EmergencyHealthInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: String,
    val sex: String,
    val address: String,
    val medications: String,
    val medicalConditions: String,
    val allergies: String,
    val phoneNumbers: String,
    val insuranceInfo: String
)