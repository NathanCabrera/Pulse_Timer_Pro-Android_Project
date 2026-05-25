package com.example.pulsetimer.timer

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pulsetimer.data.AppDatabase
import com.example.pulsetimer.timer.ConfigurationFragment
import com.example.pulsetimer.history.HistoryActivity
import com.example.pulsetimer.presets.PresetsActivity
import com.example.pulsetimer.R
import com.example.pulsetimer.data.WorkoutSession
import com.example.pulsetimer.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), TimerControls {

    private lateinit var binding: ActivityMainBinding

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0

    private var roundLengthInMillis: Long = 0
    private var restLengthInMillis: Long = 0
    private var bellIntervalInMillis: Long = 0

    private var totalRounds: Int = 0
    private var currentRound: Int = 1

    private var isRunning = false
    private var isResting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FRAGMENTS
        if (savedInstanceState == null) {

            val configFragment = ConfigurationFragment()

            if (intent.hasExtra("roundLength")) {
                val bundle = Bundle().apply {
                    putInt("roundLength", intent.getIntExtra("roundLength", 1))
                    putInt("restLength", intent.getIntExtra("restLength", 10))
                    putInt("bellInterval", intent.getIntExtra("bellInterval", 5))
                    putInt("totalRounds", intent.getIntExtra("totalRounds", 1))
                }
                configFragment.arguments = bundle
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.timerFragmentContainer, TimerDisplayFragment())
                .replace(R.id.configFragmentContainer, configFragment)
                .commit()
        }

        // NAVIGATION
        binding.btnTimer.isEnabled = false

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.btnPresets.setOnClickListener {
            startActivity(Intent(this, PresetsActivity::class.java))
        }

        // RESTORE STATE
        if (savedInstanceState != null) {
            timeLeftInMillis = savedInstanceState.getLong("timeLeft")
            currentRound = savedInstanceState.getInt("currentRound")
            isRunning = savedInstanceState.getBoolean("isRunning")
            isResting = savedInstanceState.getBoolean("isResting")

            roundLengthInMillis = savedInstanceState.getLong("roundLength")
            restLengthInMillis = savedInstanceState.getLong("restLength")
            totalRounds = savedInstanceState.getInt("totalRounds")
            bellIntervalInMillis = savedInstanceState.getLong("bellInterval")

            updateUI()

            if (isRunning) startTimer()
        }
    }

    // START
    override fun onStartClicked() {
        if (!isRunning) {
            if (timeLeftInMillis == 0L) {
                setupTimerValues()
            }
            startTimer()
        }
    }

    // PAUSE
    override fun onPauseClicked() {
        pauseTimer()
    }

    // RESET
    override fun onResetClicked() {
        resetTimer()
    }

    // END
    override fun onEndClicked() {
        endSession()
    }

    private fun setupTimerValues() {
        val configFragment = supportFragmentManager
            .findFragmentById(R.id.configFragmentContainer) as ConfigurationFragment

        val roundMinutes = configFragment.getRoundLength()
        val restSeconds = configFragment.getRestDuration()
        val totalRoundsValue = configFragment.getTotalRounds()
        val bellSeconds = configFragment.getBellInterval()

        roundLengthInMillis = roundMinutes * 60 * 1000L
        restLengthInMillis = restSeconds * 1000L
        totalRounds = totalRoundsValue
        bellIntervalInMillis = bellSeconds * 1000L

        currentRound = 1
        timeLeftInMillis = roundLengthInMillis

        updateUI()
    }

    private fun playBell() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.bell)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { it.release() }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateUI()

                if (!isResting && bellIntervalInMillis > 0) {
                    if (timeLeftInMillis % bellIntervalInMillis < 1000) {
                        playBell()
                    }
                }
            }

            override fun onFinish() {
                if (!isResting) {
                    if (currentRound < totalRounds) {
                        isResting = true
                        timeLeftInMillis = restLengthInMillis
                        startTimer()
                    } else {
                        endSession()
                    }
                } else {
                    isResting = false
                    currentRound++
                    timeLeftInMillis = roundLengthInMillis
                    startTimer()
                }
            }
        }.start()

        isRunning = true
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        isRunning = false
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        isRunning = false
        timeLeftInMillis = if (isResting) restLengthInMillis else roundLengthInMillis
        updateUI()
    }

    private fun endSession() {
        countDownTimer?.cancel()
        isRunning = false
        isResting = false

        val db = AppDatabase.Companion.getDatabase(this)

        val totalDuration = (roundLengthInMillis * totalRounds) / 60000

        val session = WorkoutSession(
            date = SimpleDateFormat("MMM dd", Locale.getDefault())
                .format(Date()),
            rounds = totalRounds,
            duration = totalDuration.toInt(),
            roundLength = (roundLengthInMillis / 60000).toInt(),
            restLength = (restLengthInMillis / 1000).toInt(),
            bellInterval = (bellIntervalInMillis / 1000).toInt()
        )

        lifecycleScope.launch {
            db.workoutDao().insertSession(session)
        }

        currentRound = 1
        timeLeftInMillis = 0
        updateUI()
    }

    private fun updateUI() {
        val fragment = supportFragmentManager
            .findFragmentById(R.id.timerFragmentContainer) as? TimerDisplayFragment

        fragment?.updateUI(
            timeLeftInMillis,
            currentRound,
            totalRounds,
            isResting,
            restLengthInMillis,
            roundLengthInMillis
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong("timeLeft", timeLeftInMillis)
        outState.putInt("currentRound", currentRound)
        outState.putBoolean("isRunning", isRunning)
        outState.putBoolean("isResting", isResting)

        outState.putLong("roundLength", roundLengthInMillis)
        outState.putLong("restLength", restLengthInMillis)
        outState.putInt("totalRounds", totalRounds)
        outState.putLong("bellInterval", bellIntervalInMillis)
    }
}