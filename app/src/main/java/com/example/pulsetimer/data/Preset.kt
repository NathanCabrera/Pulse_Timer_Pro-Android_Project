package com.example.pulsetimer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "presets")
data class Preset(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val roundLength: Int,
    val restLength: Int,
    val bellInterval: Int,
    val totalRounds: Int
)