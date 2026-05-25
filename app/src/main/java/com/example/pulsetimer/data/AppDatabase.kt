package com.example.pulsetimer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pulsetimer.data.Preset
import com.example.pulsetimer.data.PresetDao
import com.example.pulsetimer.data.WorkoutDao
import com.example.pulsetimer.data.WorkoutSession

@Database(
    entities = [WorkoutSession::class, Preset::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao
    abstract fun presetDao(): PresetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pulse_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}