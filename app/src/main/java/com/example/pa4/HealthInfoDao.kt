package com.example.pa4

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HealthInfoDao {
    @Insert
    suspend fun insertHealthInfo(info: EmergencyHealthInfo)

    @Query("SELECT * FROM emergency_health_info WHERE id = :id")
    suspend fun getHealthInfoById(id: Int): EmergencyHealthInfo?
}
