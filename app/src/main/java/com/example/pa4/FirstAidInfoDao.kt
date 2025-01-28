package com.example.pa4

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FirstAidInfoDao {
    // Correctly using a subquery to delete a single instance based on the term_key
    @Query("DELETE FROM first_aid_info WHERE id = (SELECT id FROM first_aid_info WHERE term_key = :termKey ORDER BY id LIMIT 1)")
    suspend fun deleteOneInstance(termKey: String)

    // New method to get information by ID
    @Query("SELECT * FROM first_aid_info WHERE id = :id")
    suspend fun getInfoById(id: Int): FirstAidInfo?

    // Method to get all first aid information, ordering by term_key for consistency
    @Query("SELECT * FROM first_aid_info ORDER BY term_key ASC")
    suspend fun getAllFirstAidInfo(): List<FirstAidInfo>

    // Adjusted search method to work with existing columns, using LIKE on overview_key, action_key, etc.
    @Query("""
    SELECT * FROM first_aid_info WHERE 
    term_key LIKE '%' || :query || '%' OR 
    overview_key LIKE '%' || :query || '%' OR 
    action_key LIKE '%' || :query || '%' OR 
    symptoms_key LIKE '%' || :query || '%' OR 
    triggers_key LIKE '%' || :query || '%' OR 
    treatment_key LIKE '%' || :query || '%' OR 
    prevention_key LIKE '%' || :query || '%'
""")
    suspend fun searchFirstAidInfoByTerm(query: String): List<FirstAidInfo>


    // Method to get the count of all entries in the table
    @Query("SELECT COUNT(id) FROM first_aid_info")
    suspend fun getCount(): Int

    // Method to insert one or more FirstAidInfo objects into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg infos: FirstAidInfo)

    // Method to delete a specific topic by term_key
    @Query("DELETE FROM first_aid_info WHERE term_key = :termKey")
    suspend fun deleteByTerm(termKey: String)
}