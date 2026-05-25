package com.example.pulsetimer.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pulsetimer.data.AppDatabase
import com.example.pulsetimer.data.WorkoutSession
import com.example.pulsetimer.databinding.ActivitySessionDetailBinding
import kotlinx.coroutines.launch

class SessionDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySessionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySessionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.Companion.getDatabase(this)

        // BACK BUTTON
        binding.btnBack.setOnClickListener {
            finish()
        }

        // GET DATA
        val id = intent.getIntExtra("id", -1)

        val date = intent.getStringExtra("date")
        val rounds = intent.getIntExtra("rounds", 0)
        val duration = intent.getIntExtra("duration", 0)

        val roundLength = intent.getIntExtra("roundLength", 0)
        val restLength = intent.getIntExtra("restLength", 0)
        val bellInterval = intent.getIntExtra("bellInterval", 0)

        // SET UI
        binding.tvDate.text = "📅 Date: $date"
        binding.tvRounds.text = "🥊 Total Rounds: $rounds"
        binding.tvDuration.text = "⏱ Total Time: $duration min"

        binding.tvExtra.text =
            "🥊 Round Length: $roundLength min\n" +
                    "🛑 Rest: $restLength sec\n" +
                    "🔔 Bell Interval: $bellInterval sec"

        // DELETE BUTTON
        binding.btnDeleteSession.setOnClickListener {
            lifecycleScope.launch {

                val session = WorkoutSession(
                    id = id,
                    date = "",
                    rounds = 0,
                    duration = 0,
                    roundLength = 0,
                    restLength = 0,
                    bellInterval = 0
                )

                db.workoutDao().deleteSession(session)
                finish()
            }
        }
    }
}