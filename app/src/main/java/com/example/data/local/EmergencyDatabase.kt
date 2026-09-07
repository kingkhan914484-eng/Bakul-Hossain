package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [EmergencyContactEntity::class, AuditLogEntity::class],
    version = 2,
    exportSchema = false
)
abstract class EmergencyDatabase : RoomDatabase() {
    abstract fun contactDao(): EmergencyContactDao
    abstract fun auditLogDao(): AuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: EmergencyDatabase? = null

        fun getInstance(context: Context): EmergencyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmergencyDatabase::class.java,
                    "bm_emergency_directory.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
