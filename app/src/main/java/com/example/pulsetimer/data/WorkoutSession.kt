package com.example.pulsetimer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: String,
    val rounds: Int,
    val duration: Int,

    val roundLength: Int,
    val restLength: Int,
    val bellInterval: Int
)