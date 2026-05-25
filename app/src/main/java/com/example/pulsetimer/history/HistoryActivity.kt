package com.example.pulsetimer.history

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pulsetimer.data.AppDatabase
import com.example.pulsetimer.history.HistoryListFragment
import com.example.pulsetimer.presets.PresetsActivity
import com.example.pulsetimer.R
import com.example.pulsetimer.databinding.ActivityHistoryBinding
import com.example.pulsetimer.timer.MainActivity
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FRAGMENT
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HistoryListFragment())
                .commit()
        }

        // NAVIGATION
        binding.btnHistory.isEnabled = false

        binding.btnTimer.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.btnPresets.setOnClickListener {
            startActivity(Intent(this, PresetsActivity::class.java))
        }

        // CLEAR ALL HISTORY
        binding.btnClearHistory.setOnClickListener {
            val db = AppDatabase.Companion.getDatabase(this)

            lifecycleScope.launch {
                db.workoutDao().deleteAllSessions()
                reloadFragment()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reloadFragment()
    }

    private fun reloadFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HistoryListFragment())
            .commit()
    }
}