package com.example.pa4

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Update the entities list and increment the version number
@Database(entities = [FirstAidInfo::class, EmergencyHealthInfo::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun firstAidInfoDao(): FirstAidInfoDao
    abstract fun healthInfoDao(): HealthInfoDao  // Ensure this DAO is defined as you require


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Example migration, adding a column
                db.execSQL("ALTER TABLE first_aid_info ADD COLUMN newColumn TEXT DEFAULT 'defaultValue'")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "first_aid_info"
                )
                    .addMigrations(MIGRATION_2_3)  // Make sure this is the corrected migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}