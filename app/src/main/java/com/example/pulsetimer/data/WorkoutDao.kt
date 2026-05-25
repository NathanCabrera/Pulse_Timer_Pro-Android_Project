package com.example.pulsetimer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pulsetimer.data.WorkoutSession

@Dao
interface WorkoutDao {

    @Insert
    suspend fun insertSession(session: WorkoutSession)

    @Query("SELECT * FROM sessions ORDER BY id DESC")
    suspend fun getAllSessions(): List<WorkoutSession>

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()

    @Delete
    suspend fun deleteSession(session: WorkoutSession)
}