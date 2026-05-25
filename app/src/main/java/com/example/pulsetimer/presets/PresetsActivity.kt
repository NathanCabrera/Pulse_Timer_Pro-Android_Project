package com.example.pulsetimer.presets

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pulsetimer.data.AppDatabase
import com.example.pulsetimer.data.Preset
import com.example.pulsetimer.R
import com.example.pulsetimer.databinding.ActivityPresetsBinding
import com.example.pulsetimer.history.HistoryActivity
import com.example.pulsetimer.timer.ConfigurationFragment
import com.example.pulsetimer.timer.MainActivity
import kotlinx.coroutines.launch

class PresetsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPresetsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPresetsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.Companion.getDatabase(this)

        // CONFIGURATION FRAGMENT
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.configFragmentContainer, ConfigurationFragment())
                .commit()
        }

        // NAVIGATION
        binding.btnPresets.isEnabled = false

        binding.btnTimer.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        // SETUP RECYCLER
        binding.recyclerPresets.layoutManager = LinearLayoutManager(this)

        // LOAD PRESETS
        loadPresets(db)

        // SAVE PRESET
        binding.btnSavePreset.setOnClickListener {

            val configFragment = supportFragmentManager
                .findFragmentById(R.id.configFragmentContainer) as ConfigurationFragment

            val preset = Preset(
                roundLength = configFragment.getRoundLength(),
                restLength = configFragment.getRestDuration(),
                bellInterval = configFragment.getBellInterval(),
                totalRounds = configFragment.getTotalRounds()
            )

            lifecycleScope.launch {
                db.presetDao().insertPreset(preset)
                loadPresets(db)
            }
        }

        // CLEAR PRESETS
        binding.btnClearPresets.setOnClickListener {
            lifecycleScope.launch {
                db.presetDao().deleteAllPresets()
                loadPresets(db)
            }
        }
    }

    // LOAD FUNCTION
    private fun loadPresets(db: AppDatabase) {
        lifecycleScope.launch {
            val presets = db.presetDao().getAllPresets()
            binding.recyclerPresets.adapter = PresetAdapter(presets)
        }
    }
}