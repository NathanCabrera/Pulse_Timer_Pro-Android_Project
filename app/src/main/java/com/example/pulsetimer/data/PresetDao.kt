package com.example.pulsetimer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PresetDao {

    @Insert
    suspend fun insertPreset(preset: Preset)

    @Query("SELECT * FROM presets")
    suspend fun getAllPresets(): List<Preset>

    @Query("DELETE FROM presets")
    suspend fun deleteAllPresets()
}